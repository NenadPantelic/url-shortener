package org.pisibp.demo.urlshortener.dto.urlsafety;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record URLSafetyReport(@JsonProperty("status") URLSafetyStatus status,
                              @JsonProperty("safety_params") Map<String, URLSafetyStatus> safetyParams) {
}
