package org.pisibp.demo.urlshortener.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.pisibp.demo.urlshortener.constant.HttpConstant;
import org.pisibp.demo.urlshortener.dto.URLShortenerRequest;
import org.pisibp.demo.urlshortener.dto.URLShortenerResponse;
import org.pisibp.demo.urlshortener.service.URLShortenerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(HttpConstant.API_V1_PATH + HttpConstant.URL_SHORTENER_PATH)
@CrossOrigin(value = "http://localhost:3000", maxAge = 3600)
public class URLShortenerController {

    private final URLShortenerService urlShortenerService;

    public URLShortenerController(URLShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public URLShortenerResponse makeShortURL(@Valid @RequestBody URLShortenerRequest urlShortenerRequest) {
        log.info("Received a request to make short url...");
        return urlShortenerService.makeShortUrl(urlShortenerRequest);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getCompleteURL(@PathVariable("shortUrl") String shortUrl) {
        log.info("Received a request to get complete url...");

        String completeUrl = urlShortenerService.getCompleteUrl(shortUrl);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", completeUrl);

        return ResponseEntity.status(302) // temporary redirect
                .headers(responseHeaders)
                .build();
    }
}
