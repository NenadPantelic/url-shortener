package org.pisibp.demo.urlshortener.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.pisibp.demo.urlshortener.client.UrlCheckerClient;
import org.pisibp.demo.urlshortener.dto.URLShortenerRequest;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyReport;
import org.pisibp.demo.urlshortener.exception.ApiException;
import org.pisibp.demo.urlshortener.generator.IdGenerator;
import org.pisibp.demo.urlshortener.mapper.PublicURLMapper;
import org.pisibp.demo.urlshortener.model.ShortURL;
import org.pisibp.demo.urlshortener.repository.ShortURLRepository;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.pisibp.demo.urlshortener.util.URLBase62Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class URLShortenerServiceImpl implements URLShortenerService {

    private final PublicURLMapper urlMapper;
    private final ShortURLRepository shortUrlRepository;
    private final IdGenerator idGenerator;

    private final UrlCheckerClient urlCheckerClient;

    public URLShortenerServiceImpl(PublicURLMapper urlMapper,
                                   ShortURLRepository shortUrlRepository,
                                   IdGenerator idGenerator,
                                   UrlCheckerClient urlCheckerClient) {
        this.urlMapper = urlMapper;
        this.shortUrlRepository = shortUrlRepository;
        this.idGenerator = idGenerator;
        this.urlCheckerClient = urlCheckerClient;
    }

    @Override
    public URLShortenerResponse makeShortUrl(URLShortenerRequest urlShortenerRequest) {
        log.info("Make short URL[input = {}]", urlShortenerRequest);

        final String longUrl = urlShortenerRequest.url();
        URLSafetyReport urlSafetyReport = urlCheckerClient.checkUrlSafety(longUrl);

        final long id = idGenerator.getNext();
        final String shortenedUrlIdentifier = URLBase62Converter.convert(id);

        ShortURL shortUrl = ShortURL.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortenedUrlIdentifier)
                .build();
        shortUrl = insertOrGet(shortUrl);

        return urlMapper.mapToPublicUrl(shortUrl.getShortUrl(), urlSafetyReport);
    }

    @Override
    public String getCompleteUrl(String url) {
        log.info("Get long url[short URL = {}]", url);
        return shortUrlRepository.findByShortUrl(url)
                .orElseThrow(() -> ApiException.NOT_FOUND_EXCEPTION)
                .getLongUrl();
    }

    private ShortURL insertOrGet(ShortURL shortUrl) {
        try {
            return shortUrlRepository.save(shortUrl);
        } catch (DataIntegrityViolationException e) {
            log.warn("Short url[id = {}, long url = {}, short url = {}] is already stored.",
                    shortUrl.getId(), shortUrl.getLongUrl(), shortUrl.getShortUrl());
            return shortUrlRepository.findByLongUrl(shortUrl.getLongUrl());
        }
    }
}
