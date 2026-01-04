package com.pos.shared;

import java.security.SecureRandom;

public class Utils {

    static final String ALPHANUMERIC = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final SecureRandom RANDOM = new SecureRandom();

    public static String generateIdentifier(int n) {
        if (n < 1) throw new IllegalArgumentException("Length must be at least 1");
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int randomIdx = RANDOM.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(randomIdx));
        }
        return sb.toString();
    }
}
