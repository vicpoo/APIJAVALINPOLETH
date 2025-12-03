//JWTUtil.java
package com.poleth.api.util;

import com.poleth.api.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET_KEY = "miClaveSecretaMuySeguraParaJWT2024PolethAPIQueDebeSerMuyLargaParaSeguridad";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    public static String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_TIME);

        JwtBuilder builder = Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("id", usuario.getIdUsuario())
                .claim("rol", usuario.getRol().getTitulo())
                .claim("rolId", usuario.getRol().getIdRoles())
                .claim("email", usuario.getEmail())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(KEY, SignatureAlgorithm.HS256);

        String token = builder.compact();
        System.out.println("🔐 Token generado para usuario: " + usuario.getUsername());

        // Debug: mostrar el contenido del token
        try {
            Claims claims = parseToken(token);
            System.out.println("📋 Contenido del JWT:");
            System.out.println("   - Usuario: " + claims.getSubject());
            System.out.println("   - ID Usuario: " + claims.get("id"));
            System.out.println("   - Rol: " + claims.get("rol"));
            System.out.println("   - Email: " + claims.get("email"));
        } catch (Exception e) {
            System.err.println("❌ Error al parsear token para debug: " + e.getMessage());
        }

        return token;
    }

    public static boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public static String obtenerUsuarioDesdeToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public static Integer obtenerIdDesdeToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("id", Integer.class);
    }

    public static String obtenerRolDesdeToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("rol", String.class);
    }

    public static Integer obtenerRolIdDesdeToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("rolId", Integer.class);
    }

    public static String obtenerEmailDesdeToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("email", String.class);
    }

    // Método auxiliar para parsear el token
    private static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}