package org.pisibp.demo.urlshortener.mapper;

import org.pisibp.demo.urlshortener.properties.UrlConfigProperties;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.springframework.stereotype.Component;

@Component
public class PublicUrlMapper {

    private final UrlConfigProperties urlConfig;

    public PublicUrlMapper(UrlConfigProperties urlConfig) {
        this.urlConfig = urlConfig;
    }

    public URLShortenerResponse mapToPublicUrl(String url, UrlSafetyReport urlSafetyReport) {
        return new URLShortenerResponse(
                String.format("%s/%s", urlConfig.getBaseUrl(), url), urlSafetyReport
        );
    }
}
