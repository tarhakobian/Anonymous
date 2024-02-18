package com.anonymous.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.anonymous.exception.StorageServiceException;
import com.anonymous.properties.S3ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing file storage on AWS S3.
 */
@Service
public class S3StorageService {
    private final AmazonS3 space;

    private final S3ConfigurationProperties s3ConfigurationProperties;

    /**
     * Constructs a new instance of S3StorageService with the provided S3 configuration properties.
     *
     * @param s3ConfigurationProperties The configuration properties for the S3 bucket.
     */
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

    /**
     * Uploads a file to the specified directory in the S3 bucket.
     *
     * @param multipartFile The file to upload.
     * @param directory     The directory in the bucket to upload the file to.
     * @return The URL of the uploaded file.
     * @throws StorageServiceException if an error occurs during the upload process.
     */
    public String upload(MultipartFile multipartFile, String directory) {
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

    /**
     * Deletes a file from the S3 bucket.
     *
     * @param url The URL of the file to delete.
     */
    public void delete(String url) {
        String bucketName = s3ConfigurationProperties.getBucket();

        // Extracts objectKey from URL: e.g., /posts/{uuid}.jpg or "/frames/{uuid}.jpg
        String objectKey = url.substring(51);

        space.deleteObject(bucketName, objectKey);
    }
}
