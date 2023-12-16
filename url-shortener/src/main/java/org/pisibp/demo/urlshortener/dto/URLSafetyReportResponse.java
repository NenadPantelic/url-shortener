package org.pisibp.demo.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyStatus;

import java.util.Map;


public record URLSafetyReportResponse(URLSafetyStatus status,
                                      @JsonProperty("safetyParams") Map<String, URLSafetyStatus> safetyParams) {

    public Map<String, URLSafetyStatus> getSafetyParams() {
        return Map.of(
                "syntaxIsCorrect", safetyParams.get("syntax_is_correct"),
                "siteIsAlive", safetyParams.get("site_is_alive")
        );
    }
}
