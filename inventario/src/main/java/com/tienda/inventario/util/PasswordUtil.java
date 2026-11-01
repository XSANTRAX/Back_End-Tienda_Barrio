package com.tienda.inventario.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String cifrar(String passwordPlano) {
        if (passwordPlano == null || passwordPlano.isBlank()) {
            return null;
        }
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
    }

    public static boolean verificar(String passwordPlano, String hash) {
        if (passwordPlano == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(passwordPlano, hash);
    }
}