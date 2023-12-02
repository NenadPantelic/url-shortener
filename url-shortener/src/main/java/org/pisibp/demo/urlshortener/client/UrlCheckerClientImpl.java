package org.pisibp.demo.urlshortener.client;

import lombok.extern.slf4j.Slf4j;
import org.pisibp.demo.urlshortener.constant.HttpConstant;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlCheckerRequest;
import org.pisibp.demo.urlshortener.dto.urlsafety.UrlSafetyReport;
import org.pisibp.demo.urlshortener.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class UrlCheckerClientImpl implements UrlCheckerClient {

    private final WebClient webClient;

    public UrlCheckerClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public UrlSafetyReport checkUrlSafety(String url) {
        log.info("Checking the safety of the {}...", url);
        WebClient.RequestBodySpec uriSpec = webClient.post().uri(HttpConstant.URL_CHECKER_PATH);

        WebClient.RequestHeadersSpec<?> headersSpec = uriSpec.body(
                Mono.just(new UrlCheckerRequest(url)), UrlCheckerRequest.class
        ).accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);

        Mono<UrlSafetyReport> responseBody = headersSpec.exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(UrlSafetyReport.class);
            } else if (response.statusCode().is4xxClientError()) {
                throw new ApiException("Bad request.", 400);
            } else {
                return response.createException().flatMap(Mono::error);
            }
        });

        try {
            return responseBody.toFuture().get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Could not check the safety of the given url.", e);
            throw new ApiException("Internal server error.", 500);
        }
    }
}
