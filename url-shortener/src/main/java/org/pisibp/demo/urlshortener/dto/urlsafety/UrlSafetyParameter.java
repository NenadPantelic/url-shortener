package org.pisibp.demo.urlshortener.dto.urlsafety;

public record UrlSafetyParameter(
        String key,
        boolean value) {

//
//    public static final UrlSafetyParameter SYNTAX_IS_CORRECT = new UrlSafetyParameter(
//            "Syntax correctness",
//    );
//    public static final UrlSafetyParameter SECURE_CONNECTION;
//    public static final UrlSafetyParameter SITE_IS_ALIVE;
}
