package org.pisibp.demo.urlshortener.service;

import org.pisibp.demo.urlshortener.dto.URLShortenerRequest;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;

public interface URLShortenerService {

    URLShortenerResponse makeShortURL(URLShortenerRequest urlShortenerRequest);

    String getCompleteURL(String shortUrl);
}
