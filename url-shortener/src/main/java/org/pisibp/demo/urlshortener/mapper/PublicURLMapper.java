package org.pisibp.demo.urlshortener.mapper;

import org.pisibp.demo.urlshortener.dto.URLSafetyReportResponse;
import org.pisibp.demo.urlshortener.properties.URLConfigProperties;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyReport;
import org.springframework.stereotype.Component;

@Component
public class PublicURLMapper {

    private final URLConfigProperties urlConfig;

    public PublicURLMapper(URLConfigProperties urlConfig) {
        this.urlConfig = urlConfig;
    }

    public URLShortenerResponse mapToPublicUrl(String url, URLSafetyReport urlSafetyReport) {
        return new URLShortenerResponse(
                String.format("%s/%s", urlConfig.getBaseUrl(), url),
                new URLSafetyReportResponse(urlSafetyReport.status(), urlSafetyReport.safetyParams())
        );
    }
}
