package com.photomemories.aws.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.photomemories.aws.AwsFileServices;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class AwsFileServiceImpl implements AwsFileServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsFileServiceImpl.class);
    private final AmazonS3 s3;

    //Dependency Injection: s3 is injected and singleton pattern is followed
    @Autowired
    public AwsFileServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    //Upload a photo to the S3 bucket
    @Override
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            optionalMetaData.ifPresent((map -> {
                if (!map.isEmpty()) {
                    map.forEach(objectMetadata::addUserMetadata);
                }
            }));
            s3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException error) {
            LOGGER.error("[AWS Service log] save method, Could not upload the file to the S3 bucket, with error {}", error.getMessage());
            throw new IllegalStateException("[AWS Service Error] save method, Could not execute the request ", error);
        }
    }

    //Downloads a photo from the S3 bucket
    @Override
    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (AmazonServiceException | IOException error) {
            LOGGER.error("[AWS Service log] download method, Could not download from the S3 bucket, with error {}", error.getMessage());
            throw new IllegalStateException("[AWS Service Error] download method, Could not execute the request ", error);
        }
    }

    //Removes a photo from the S3 bucket
    @Override
    public void deletePhotoFromFolder(String bucketName, String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
            s3.deleteObject(deleteObjectRequest);
        } catch (AmazonServiceException error) {
            LOGGER.error("[AWS Service log] deletePhotoFromFolder method, Could not delete the photo from the S3 bucket, with error {}", error.getMessage());
            throw new IllegalStateException("[AWS Service Error] deletePhotoFromFolder method, Could not execute the request ", error);
        }
    }

    //Removes a user's folder from the S3 bucket
    @Override
    public void deleteUserFolder(String bucketName, String path) {
        try {
            for (S3ObjectSummary file : s3.listObjects(bucketName, path).getObjectSummaries()){
                s3.deleteObject(bucketName, file.getKey());
            }
        } catch (AmazonServiceException error) {
            LOGGER.error("[AWS Service log] deleteUserFolder method, Could not delete the folder from the S3 bucket, with error {}", error.getMessage());
            throw new IllegalStateException("[AWS Service Error] deleteUserFolder method, Could not execute the request ", error);
        }
    }

    //Shares a photo to another folder on the S3 bucket
    @Override
    public void sharePhoto(String fromBucketName, String toBucketName, String key) {
        try {
            s3.copyObject(fromBucketName, key, toBucketName, key);
        } catch (AmazonServiceException error) {
            LOGGER.error("[AWS Service log] deleteUserFolder method, Could not share the photo on S3 bucket, with error {}", error.getMessage());
            throw new IllegalStateException("[AWS Service Error] deleteUserFolder method, Could not execute the request ", error);
        }
    }
}
