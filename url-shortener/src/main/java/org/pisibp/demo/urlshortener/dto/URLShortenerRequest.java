package org.pisibp.demo.urlshortener.dto;


import jakarta.validation.constraints.NotBlank;

public record URLShortenerRequest(@NotBlank(message = "URL must be provided.") String url) {
}
