package org.pisibp.demo.urlshortener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@ConfigurationProperties(prefix = "url-checker")
@ConfigurationPropertiesScan
public class URLCheckerConfigProperties {

    private final String url;
    private final String headerKey;
    private final String session;
    private final int timeoutInMillis;
    private final int maxRetry;
    // TODO
    private final int backoff;
}
