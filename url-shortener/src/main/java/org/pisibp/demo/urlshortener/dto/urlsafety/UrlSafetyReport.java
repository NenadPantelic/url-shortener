package org.pisibp.demo.urlshortener.dto.urlsafety;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record UrlSafetyReport(@JsonProperty("status") UrlSafetyStatus status,
                              @JsonProperty("safety_params") Map<String, UrlSafetyStatus> safetyParams) {
}
