package org.pisibp.demo.urlshortener.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pisibp.demo.urlshortener.client.UrlCheckerClient;
import org.pisibp.demo.urlshortener.dto.URLSafetyReportResponse;
import org.pisibp.demo.urlshortener.dto.URLShortenerRequest;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyStatus;
import org.pisibp.demo.urlshortener.exception.ApiException;
import org.pisibp.demo.urlshortener.generator.IdGenerator;
import org.pisibp.demo.urlshortener.mapper.PublicUrlMapper;
import org.pisibp.demo.urlshortener.model.ShortUrl;
import org.pisibp.demo.urlshortener.repository.ShortUrlRepository;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class URLShortenerServiceImplTest {

    private static final String PUBLIC_URL_BASE = "http://pub.lic";

    @Mock
    private PublicUrlMapper urlMapper;

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private UrlCheckerClient urlCheckerClient;

    private URLShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        urlShortenerService = new URLShortenerServiceImpl(
                urlMapper, shortUrlRepository, idGenerator, urlCheckerClient
        );
    }

    @Test
    @DisplayName("Tests whether the url is properly shortened.")
    public void givenLongUrlWhenShortUrlCalledShouldReturnShortenedUrl() {
        // given
        final String longUrl = "http://test.example/veryveryverylongurl";
        final URLShortenerRequest urlShortenerRequest = new URLShortenerRequest(longUrl);

        final long id = 125;
        // 125_10 -> 21_62; 00000000021
        final String shortUrl = "00000000021";

        // when
        Mockito.doReturn(id).when(idGenerator).getNext();
        ShortUrl expectedShortUrl = ShortUrl.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        final UrlSafetyStatus overallStatus = UrlSafetyStatus.NO;
        final Map<String, UrlSafetyStatus> urlSafetyStatuses = Map.of(
                "param-1", UrlSafetyStatus.YES,
                "param-2", UrlSafetyStatus.NO,
                "param-3", UrlSafetyStatus.UNKNOWN
        );
        final UrlSafetyReport urlSafetyReport = new UrlSafetyReport(
                overallStatus, urlSafetyStatuses
        );
        Mockito.doReturn(urlSafetyReport).when(urlCheckerClient).checkUrlSafety(longUrl);

        Mockito.doReturn(expectedShortUrl).when(shortUrlRepository).save(expectedShortUrl);

        final String publicUrl = String.format("%s/%s", PUBLIC_URL_BASE, shortUrl);
        URLShortenerResponse expectedUrlShortenerResponse = new URLShortenerResponse(
                publicUrl,
                new URLSafetyReportResponse(overallStatus, urlSafetyStatuses)
        );
        Mockito.doReturn(expectedUrlShortenerResponse)
                .when(urlMapper)
                .mapToPublicUrl(shortUrl, urlSafetyReport);


        AssertionsForClassTypes
                .assertThat(urlShortenerService.makeShortUrl(urlShortenerRequest))
                .isEqualTo(expectedUrlShortenerResponse);
    }

    @Test
    @DisplayName("Provides a URL that has already been shortened and stored.")
    public void givenLongUrlThatIsAlreadyStoredWhenShortUrlCalledShouldReturnStoredShortenedUrl() {
        // given
        final String longUrl = "http://test.example/veryveryverylongurl";
        final URLShortenerRequest urlShortenerRequest = new URLShortenerRequest(longUrl);

        final long id = 125;
        // 125_10 -> 21_62; 00000000021
        final String shortUrl = "00000000021";

        // when
        Mockito.doReturn(id).when(idGenerator).getNext();
        ShortUrl expectedShortUrl = ShortUrl.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        final UrlSafetyStatus overallStatus = UrlSafetyStatus.NO;
        final Map<String, UrlSafetyStatus> urlSafetyStatuses = Map.of(
                "param-1", UrlSafetyStatus.YES,
                "param-2", UrlSafetyStatus.NO,
                "param-3", UrlSafetyStatus.UNKNOWN
        );
        final UrlSafetyReport urlSafetyReport = new UrlSafetyReport(
                overallStatus, urlSafetyStatuses
        );
        Mockito.doReturn(urlSafetyReport).when(urlCheckerClient).checkUrlSafety(longUrl);

        // throw an exception as it's already stored
        Mockito.doThrow(new DataIntegrityViolationException("Unique constraint violated"))
                .when(shortUrlRepository)
                .save(expectedShortUrl);

        Mockito.doReturn(expectedShortUrl).when(shortUrlRepository).findByLongUrl(longUrl);

        final String publicUrl = String.format("%s/%s", PUBLIC_URL_BASE, shortUrl);
        URLShortenerResponse expectedUrlShortenerResponse = new URLShortenerResponse(
                publicUrl,
                new URLSafetyReportResponse(overallStatus, urlSafetyStatuses)
        );
        Mockito.doReturn(expectedUrlShortenerResponse)
                .when(urlMapper)
                .mapToPublicUrl(shortUrl, urlSafetyReport);


        AssertionsForClassTypes
                .assertThat(urlShortenerService.makeShortUrl(urlShortenerRequest))
                .isEqualTo(expectedUrlShortenerResponse);
    }


    @Test
    @DisplayName("Get complete URL.")
    public void givenShortUrlWhenGetCompleteUrlCalledShouldReturnCompleteUrl() {
        // given
        final String shortUrl = "00000000021";
        final String longUrl = "http://test.example/veryveryverylongurl";

        // when
        ShortUrl expectedShortUrl = ShortUrl.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        Mockito.doReturn(Optional.of(expectedShortUrl))
                .when(shortUrlRepository)
                .findByShortUrl(shortUrl);

        AssertionsForClassTypes
                .assertThat(urlShortenerService.getCompleteUrl(shortUrl))
                .isEqualTo(longUrl);
    }


    @Test
    @DisplayName("Get complete URL when short URL is not found.")
    public void givenShortUrlWhenUrlNotFoundWhenGetCompleteUrlCalledShouldThrowNotFoundException() {
        // given
        final String shortUrl = "00000000021";

        Mockito.doReturn(Optional.empty())
                .when(shortUrlRepository)
                .findByShortUrl(shortUrl);

        AssertionsForClassTypes.assertThatThrownBy(() -> urlShortenerService.getCompleteUrl(shortUrl))
                .isEqualTo(ApiException.NOT_FOUND_EXCEPTION);
    }


}
