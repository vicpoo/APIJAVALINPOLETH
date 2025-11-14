package com.poleth.api.util;

import com.poleth.api.model.Login;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET_KEY = "miClaveSecretaMuySeguraParaJWT2024PolethAPIQueDebeSerMuyLargaParaSeguridad";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    public static String generarToken(Login login) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_TIME);

        JwtBuilder builder = Jwts.builder()
                .setSubject(login.getUsuario())
                .claim("id", login.getIdLogin())
                .claim("rol", login.getRol().getNombreRol())
                .claim("rolId", login.getRol().getIdRol())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(KEY, SignatureAlgorithm.HS256);

        // Agregar información de inquilino si existe
        if (login.getInquilino() != null && login.getInquilino().getIdInquilino() != null) {
            builder.claim("inquilinoId", login.getInquilino().getIdInquilino());
            System.out.println("✅ Agregando inquilinoId al JWT: " + login.getInquilino().getIdInquilino());
        }

        // Agregar información de propietario si existe
        if (login.getPropietario() != null && login.getPropietario().getIdPropietario() != null) {
            builder.claim("propietarioId", login.getPropietario().getIdPropietario());
            System.out.println("✅ Agregando propietarioId al JWT: " + login.getPropietario().getIdPropietario());
        }

        // Agregar información de invitado si existe
        if (login.getInvitado() != null && login.getInvitado().getIdInvitado() != null) {
            builder.claim("invitadoId", login.getInvitado().getIdInvitado());
            System.out.println("✅ Agregando invitadoId al JWT: " + login.getInvitado().getIdInvitado());
        }

        String token = builder.compact();
        System.out.println("🔐 Token generado con claims adicionales");

        // Debug: mostrar el contenido del token
        try {
            Claims claims = parseToken(token);
            System.out.println("📋 Contenido del JWT:");
            System.out.println("   - Usuario: " + claims.getSubject());
            System.out.println("   - ID Login: " + claims.get("id"));
            System.out.println("   - Rol: " + claims.get("rol"));
            System.out.println("   - InquilinoId: " + claims.get("inquilinoId"));
            System.out.println("   - PropietarioId: " + claims.get("propietarioId"));
            System.out.println("   - InvitadoId: " + claims.get("invitadoId"));
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

    // NUEVOS MÉTODOS PARA OBTENER INQUILINO Y PROPIETARIO
    public static Integer obtenerInquilinoIdDesdeToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("inquilinoId", Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer obtenerPropietarioIdDesdeToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("propietarioId", Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer obtenerInvitadoIdDesdeToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("invitadoId", Integer.class);
        } catch (Exception e) {
            return null;
        }
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