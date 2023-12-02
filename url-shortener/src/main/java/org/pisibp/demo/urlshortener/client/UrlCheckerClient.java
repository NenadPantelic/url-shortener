package org.pisibp.demo.urlshortener.client;

import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;

public interface UrlCheckerClient {

    UrlSafetyReport checkUrlSafety(String url);
}
