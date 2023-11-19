package org.pisibp.demo.urlshortener.service.impl;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.pisibp.demo.urlshortener.client.UrlCheckerClient;
import org.pisibp.demo.urlshortener.dto.URLShortenerRequest;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.pisibp.demo.urlshortener.exception.ApiException;
import org.pisibp.demo.urlshortener.generator.IdGenerator;
import org.pisibp.demo.urlshortener.mapper.PublicUrlMapper;
import org.pisibp.demo.urlshortener.model.ShortUrl;
import org.pisibp.demo.urlshortener.repository.ShortUrlRepository;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.pisibp.demo.urlshortener.util.UrlBase62Converter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class URLShortenerServiceImpl implements URLShortenerService {

    private final PublicUrlMapper urlMapper;
    private final ShortUrlRepository shortUrlRepository;
    private final IdGenerator idGenerator;

    private final UrlCheckerClient urlCheckerClient;

    public URLShortenerServiceImpl(PublicUrlMapper urlMapper,
                                   ShortUrlRepository shortUrlRepository,
                                   IdGenerator idGenerator,
                                   UrlCheckerClient urlCheckerClient) {
        this.urlMapper = urlMapper;
        this.shortUrlRepository = shortUrlRepository;
        this.idGenerator = idGenerator;
        this.urlCheckerClient = urlCheckerClient;
    }

    @Override
    public URLShortenerResponse makeShortURL(@Valid URLShortenerRequest urlShortenerRequest) {
        log.info("Make short URL[input = {}]", urlShortenerRequest);
//
//        if (longUrl == null || longUrl.isBlank()) {
//            throw ApiException.BAD_REQUEST_EXCEPTION;
//        }

        final String longUrl = urlShortenerRequest.url();
        UrlSafetyReport urlSafetyReport = urlCheckerClient.isUrlSafe(longUrl);

        final long id = idGenerator.getNext();
        final String shortenedUrlIdentifier = UrlBase62Converter.convert(id);

        ShortUrl shortUrl = ShortUrl.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortenedUrlIdentifier)
                .build();
        shortUrl = insertOrGet(shortUrl);

        return urlMapper.mapToPublicUrl(shortUrl.getShortUrl(), urlSafetyReport);
    }

    @Override
    public String getCompleteURL(String url) {
        log.info("Get long url[short URL = {}]", url);
        return shortUrlRepository.findByShortUrl(url)
                .orElseThrow(() -> ApiException.NOT_FOUND_EXCEPTION)
                .getLongUrl();
    }

    private ShortUrl insertOrGet(ShortUrl shortUrl) {
        try {
            return shortUrlRepository.save(shortUrl);
        } catch (ConstraintViolationException e) {
            log.warn("Short url[id = {}, long url = {}, short url = {}] is already stored.",
                    shortUrl.getId(), shortUrl.getLongUrl(), shortUrl.getShortUrl());
            return shortUrlRepository.findByLongUrl(shortUrl.getLongUrl());
        }

    }
}
