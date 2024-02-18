package com.anonymous.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Amazon S3 settings.
 * These properties are configured in the application.yml file.
 * <p>
 * Author : Taron Hakobyan
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "s3")
public class S3ConfigurationProperties {

    private String bucket;
    private String accessKey;
    private String secretKey;
    private String serviceEndpoint;
    private String signingRegion;
}
