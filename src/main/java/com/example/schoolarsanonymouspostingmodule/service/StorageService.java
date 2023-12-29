package com.example.schoolarsanonymouspostingmodule.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {
    private final AmazonS3 space;

    public StorageService() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("DO00NCNANMFMPYJUWHCJ",
                        "bMbbFJjtFhTxiOu/FLBO9b484a+KNRlSV/6LmJn0TWA")
        );

        space = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "sfo2.digitaloceanspaces.com", "sfo2"
                        )
                ).build();
    }

    public void getPostsFileNames() {
        ListObjectsV2Result result = space.listObjectsV2("anonymousposts", "posts");
        List<S3ObjectSummary> list = result.getObjectSummaries();
        System.out.println(list.stream().map(S3ObjectSummary::getKey).toList().size());
//
//        list.forEach(s3ObjectSummary -> System.out.println(s3ObjectSummary.getKey());
    }
}
