package org.pisibp.demo.urlshortener.dto.urlsafety;

import java.util.Map;

public record UrlSafetyReport(UrlSafetyStatus urlSafetyStatus,
                              Map<String, Boolean> safetyParams) {
}
