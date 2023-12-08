package org.pisibp.demo.urlshortener.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlCheckerRequest;
import org.pisibp.demo.urlshortener.model.ShortUrl;
import org.pisibp.demo.urlshortener.repository.DatabaseSetup;
import org.pisibp.demo.urlshortener.repository.ShortUrlRepository;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// do note - we need url-checker service up and running for these tests; it's better to use stub server for the url-checker
class URLShortenerControllerTest extends DatabaseSetup {

    @Autowired
    private URLShortenerService urlShortenerService;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void doSetup() {
        RestAssured.baseURI = "http://localhost:" + port;
        shortUrlRepository.deleteAll();
    }

    @Test
    public void testGetCompleteUrl() {
        String longUrl = "https://www.google.com/";
        String shortenedUrl = "short.url";

        ShortUrl shortUrl = ShortUrl.builder()
                .id(123456L)
                .longUrl(longUrl)
                .shortUrl(shortenedUrl)
                .build();
        shortUrlRepository.save(shortUrl);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("/api/v1/url-shortener/%s", shortenedUrl))
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetCompleteUrlWhenItIsNotStored() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("/api/v1/url-shortener/%s", "unexistent-url"))
                .then()
                .statusCode(404);
    }


    @Test
    public void testMakeShortUrl() {
        String longUrl = "https://www.google.com/";

        RestAssured.with().body(new UrlCheckerRequest(longUrl))
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/url-shortener")
                .then()
                .statusCode(201)
                .assertThat()
                .body("urlSafetyReport.status", new IsEqual<>("YES"))
                .assertThat()
                .body("url", Matchers.notNullValue());
    }

    @Test
    public void testMakeShortUrlWithEmptyUrl() {
        RestAssured.with().body(new UrlCheckerRequest(null))
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/url-shortener")
                .then()
                .statusCode(400);
    }

}