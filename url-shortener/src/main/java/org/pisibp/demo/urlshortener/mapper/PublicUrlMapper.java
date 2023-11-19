package org.pisibp.demo.urlshortener.mapper;

import org.pisibp.demo.urlshortener.config.UrlConfig;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.springframework.stereotype.Component;

@Component
public class PublicUrlMapper {

    private final UrlConfig urlConfig;

    public PublicUrlMapper(UrlConfig urlConfig) {
        this.urlConfig = urlConfig;
    }

    public URLShortenerResponse mapToPublicUrl(String url, UrlSafetyReport urlSafetyReport) {
        return new URLShortenerResponse(
                String.format("%s/%s", urlConfig.getBaseUrl(), url), urlSafetyReport
        );
    }
}
