package com.photomemories.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsConfig.class);

    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAWFFGT4GVHIR6YE7N", "9V9HNDWJDFEorOGirwvz0zGSTrhdpF4cb2JgtOyG");

        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_1).build();
    }
}
