//PasswordUtil.java
package com.poleth.api.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidHash(String hashedPassword) {
        if (hashedPassword == null) {
            return false;
        }
        return hashedPassword.startsWith("$2a$") ||
                hashedPassword.startsWith("$2b$") ||
                hashedPassword.startsWith("$2y$");
    }
}