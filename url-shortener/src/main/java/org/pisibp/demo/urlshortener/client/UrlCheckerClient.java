package org.pisibp.demo.urlshortener.client;

import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyReport;

public interface UrlCheckerClient {

    URLSafetyReport checkUrlSafety(String url);
}
