package org.pisibp.demo.urlshortener.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.pisibp.demo.urlshortener.properties.URLCheckerConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient urlCheckClient(URLCheckerConfigProperties urlCheckerConfigProperties) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, urlCheckerConfigProperties.getTimeoutInMillis())
                .responseTimeout(Duration.ofMillis(urlCheckerConfigProperties.getTimeoutInMillis()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                urlCheckerConfigProperties.getTimeoutInMillis(), TimeUnit.MILLISECONDS)
                        )
                        .addHandlerLast(new WriteTimeoutHandler(
                                urlCheckerConfigProperties.getTimeoutInMillis(), TimeUnit.MILLISECONDS)
                        )
                );


        return WebClient.builder()
                .baseUrl(urlCheckerConfigProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(urlCheckerConfigProperties.getHeaderKey(), urlCheckerConfigProperties.getSession())
                .defaultUriVariables(Collections.singletonMap("url", urlCheckerConfigProperties.getUrl()))
                .build();
    }
}
