package org.pisibp.demo.urlshortener.dto;

public record URLShortenerResponse(String url, URLSafetyReportResponse urlSafetyReport) {
}
