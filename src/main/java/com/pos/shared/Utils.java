package com.pos.shared;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.security.SecureRandom;

public class Utils {

    static final String ALPHANUMERIC = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final SecureRandom RANDOM = new SecureRandom();


    public static String generateIdentifier(int n, String delimiter) {
        if (n < 8) throw new IllegalArgumentException("Length must be at least 1");
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int randomIdx = RANDOM.nextInt(ALPHANUMERIC.length());
            if(i % 4 == 0 && i != 0) {sb.append(delimiter == null ? "-" : delimiter);}
            sb.append(ALPHANUMERIC.charAt(randomIdx));
        }
        return sb.toString();
    }

    public static @NonNull BigDecimal calculateDiscount(double discount ,BigDecimal total) {
        double divisor = discount / 100.0;
        BigDecimal discountPercent =  BigDecimal.valueOf(divisor).multiply(total);
        return total.subtract(discountPercent);
    }
}
