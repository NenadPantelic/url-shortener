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
import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyReport;
import org.pisibp.demo.urlshortener.dto.urlsafety.URLSafetyStatus;
import org.pisibp.demo.urlshortener.exception.ApiException;
import org.pisibp.demo.urlshortener.generator.IdGenerator;
import org.pisibp.demo.urlshortener.mapper.PublicURLMapper;
import org.pisibp.demo.urlshortener.model.ShortURL;
import org.pisibp.demo.urlshortener.repository.ShortURLRepository;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class URLShortenerServiceImplTest {

    private static final String PUBLIC_URL_BASE = "http://pub.lic";

    @Mock
    private PublicURLMapper urlMapper;

    @Mock
    private ShortURLRepository shortUrlRepository;

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
        ShortURL expectedShortURL = ShortURL.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        final URLSafetyStatus overallStatus = URLSafetyStatus.NO;
        final Map<String, URLSafetyStatus> urlSafetyStatuses = Map.of(
                "param-1", URLSafetyStatus.YES,
                "param-2", URLSafetyStatus.NO,
                "param-3", URLSafetyStatus.UNKNOWN
        );
        final URLSafetyReport urlSafetyReport = new URLSafetyReport(
                overallStatus, urlSafetyStatuses
        );
        Mockito.doReturn(urlSafetyReport).when(urlCheckerClient).checkUrlSafety(longUrl);

        Mockito.doReturn(expectedShortURL).when(shortUrlRepository).save(expectedShortURL);

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
        ShortURL expectedShortURL = ShortURL.builder()
                .id(id)
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        final URLSafetyStatus overallStatus = URLSafetyStatus.NO;
        final Map<String, URLSafetyStatus> urlSafetyStatuses = Map.of(
                "param-1", URLSafetyStatus.YES,
                "param-2", URLSafetyStatus.NO,
                "param-3", URLSafetyStatus.UNKNOWN
        );
        final URLSafetyReport urlSafetyReport = new URLSafetyReport(
                overallStatus, urlSafetyStatuses
        );
        Mockito.doReturn(urlSafetyReport).when(urlCheckerClient).checkUrlSafety(longUrl);

        // throw an exception as it's already stored
        Mockito.doThrow(new DataIntegrityViolationException("Unique constraint violated"))
                .when(shortUrlRepository)
                .save(expectedShortURL);

        Mockito.doReturn(expectedShortURL).when(shortUrlRepository).findByLongUrl(longUrl);

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
        ShortURL expectedShortURL = ShortURL.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .build();

        Mockito.doReturn(Optional.of(expectedShortURL))
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
