package org.pisibp.demo.urlshortener.client;

import lombok.extern.slf4j.Slf4j;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlCheckerClientImpl implements UrlCheckerClient {

    @Override
    public UrlSafetyReport isUrlSafe(String url) {
        return null;
    }
}
