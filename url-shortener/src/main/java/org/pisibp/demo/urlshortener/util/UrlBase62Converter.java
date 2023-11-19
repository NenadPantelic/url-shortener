package org.pisibp.demo.urlshortener.util;

import java.util.HashMap;
import java.util.Map;

public class UrlBase62Converter {

    private static final int BASE = 62;
    private static final int LENGTH_OF_URL= 11;
    private static final Map<Long, String> BASE_62_CONVERSION_MAP = new HashMap<>();

    static {
        // digits [0-9]
        for (long i = 0; i < 10; i++) {
            BASE_62_CONVERSION_MAP.put(i, String.valueOf(i));
        }

        long counter = 10;
        // lowercase letters [a-z]
        for (char letter = 'a'; letter <= 'z'; letter++) {
            BASE_62_CONVERSION_MAP.put(counter++, String.valueOf(letter));
        }

        // uppercase letters [A-Z]
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            BASE_62_CONVERSION_MAP.put(counter++, String.valueOf(letter));
        }

        // 0-0, 1-1, 2-2,...9-9, 10-a, 11-b,....36-A, 37-B,...61-Z
    }

    public static String convert(long value) {
        StringBuilder strBuilder = new StringBuilder();

        while (value > 0) {
            strBuilder.append(BASE_62_CONVERSION_MAP.get(value % BASE));
            value /= BASE;
        }

        // in case resulting URL is shorter than LENGTH_OF_URL chars
        strBuilder.append("0".repeat(Math.max(0, LENGTH_OF_URL - strBuilder.length())));

        return strBuilder.reverse().toString();
    }

}
