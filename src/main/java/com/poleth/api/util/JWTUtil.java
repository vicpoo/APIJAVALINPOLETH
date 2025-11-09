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

        return Jwts.builder()
                .setSubject(login.getUsuario())
                .claim("id", login.getIdLogin())
                .claim("rol", login.getRol().getNombreRol())
                .claim("rolId", login.getRol().getIdRol())
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static Integer obtenerIdDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", Integer.class);
    }

    public static String obtenerRolDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rol", String.class);
    }

    public static Integer obtenerRolIdDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rolId", Integer.class);
    }
}