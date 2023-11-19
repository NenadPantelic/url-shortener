package org.pisibp.demo.urlshortener.dto;

import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;

public record URLShortenerResponse(String url, UrlSafetyReport urlSafetyReport) {
}
