package com.example.schoolarsanonymouspostingmodule.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
