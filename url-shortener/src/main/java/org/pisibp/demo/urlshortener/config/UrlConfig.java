package org.pisibp.demo.urlshortener.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Data
//@Configuration
@ConfigurationProperties(prefix = "url-config")
@ConfigurationPropertiesScan
public class UrlConfig {

    private final String baseUrl;
}
