package org.pisibp.demo.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyStatus;

import java.util.Map;


public record URLSafetyReportResponse(UrlSafetyStatus status,
                                      @JsonProperty("safetyParams") Map<String, UrlSafetyStatus> safetyParams) {

    public Map<String, UrlSafetyStatus> getSafetyParams() {
        return Map.of(
                "syntaxIsCorrect", safetyParams.get("syntax_is_correct"),
                "siteIsAlive", safetyParams.get("site_is_alive")
        );
    }
}
