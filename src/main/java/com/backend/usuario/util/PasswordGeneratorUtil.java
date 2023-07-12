package com.backend.usuario.util;

import java.util.UUID;

public class PasswordGeneratorUtil {

    private PasswordGeneratorUtil() {}
    /**
     * @return
     */
    public static String generateNewSecretKey() {
        return UUID.randomUUID().toString();
    }
}
