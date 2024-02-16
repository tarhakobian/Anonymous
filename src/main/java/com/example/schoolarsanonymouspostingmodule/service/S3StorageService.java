package com.example.schoolarsanonymouspostingmodule.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.schoolarsanonymouspostingmodule.exception.StorageServiceException;
import com.example.schoolarsanonymouspostingmodule.properties.S3ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3StorageService {
    private final AmazonS3 space;

    private final S3ConfigurationProperties s3ConfigurationProperties;

    public S3StorageService(S3ConfigurationProperties s3ConfigurationProperties) {
        this.s3ConfigurationProperties = s3ConfigurationProperties;
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(s3ConfigurationProperties.getAccessKey(), s3ConfigurationProperties.getSecretKey())
        );

        space = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                s3ConfigurationProperties.getServiceEndpoint(),
                                s3ConfigurationProperties.getSigningRegion()
                        )
                ).build();
    }

    public String upload(MultipartFile multipartFile) {
        String directory = "posts";

        String uuid = String.valueOf(UUID.randomUUID());

        String fileName = "%s.%s".formatted(uuid, Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1]);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpeg");
        objectMetadata.setContentLength(multipartFile.getSize());


        try {
            space.putObject(new PutObjectRequest(
                    s3ConfigurationProperties.getBucket(),
                    "%s/%s".formatted(directory, fileName),
                    multipartFile.getInputStream(),
                    objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new StorageServiceException();
        }

        return "https://%s.%s/%s/%s".formatted(
                s3ConfigurationProperties.getBucket(),
                s3ConfigurationProperties.getServiceEndpoint(),
                directory,
                fileName
        );
    }

    public void delete(String url) {
        String bucketName = "anonymousposts";

        //extracts objectKey from url : ex. /posts/{uuid}.jpg
        String objectKey = url.substring(51);

        space.deleteObject(bucketName, objectKey);
    }


}