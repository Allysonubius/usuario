package com.backend.usuario.util;

import java.util.UUID;

public class PasswordGeneratorUtil {
    /**
     * @return
     */
    public static String generateNewSecretKey() {
        return UUID.randomUUID().toString();
    }
}
