package com.backend.usuario.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidatorUtil {
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+[?:\\.[a-zA-Z0-9]+]*@[?:[a-zA-Z0-9]+\\.]+[a-zA-Z]{2,7}$";

    public EmailValidatorUtil() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(final String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
