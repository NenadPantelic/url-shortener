package org.pisibp.demo.urlshortener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@ConfigurationProperties(prefix = "url-config")
@ConfigurationPropertiesScan
public class URLConfigProperties {

    private final String baseUrl;
}
