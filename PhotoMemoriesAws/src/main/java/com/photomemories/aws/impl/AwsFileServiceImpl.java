package com.photomemories.aws.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.photomemories.aws.AwsFileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class AwsFileServiceImpl implements AwsFileServices {
    private final AmazonS3 s3;

    @Autowired
    public AwsFileServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent((map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        }));
        try {
            s3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException error) {
            throw new IllegalStateException("Could not store the photo onto the cloud", error);
        }
    }

    @Override
    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Could not download from the cloud", e.getCause());
        }
    }

    @Override
    public void deletePhotoFromFolder(String bucketName, String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
        s3.deleteObject(deleteObjectRequest);
    }

    @Override
    public void deleteUserFolder(String bucketName, String path) {
        for (S3ObjectSummary file : s3.listObjects(bucketName, path).getObjectSummaries()){
            s3.deleteObject(bucketName, file.getKey());
        }
    }

    @Override
    public void sharePhoto(String fromBucketName, String toBucketName, String key) {
        try {
            s3.copyObject(fromBucketName, key, toBucketName, key);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Could not share the photo on the cloud", e.getCause());
        }
    }
}
