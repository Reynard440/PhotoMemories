package com.photomemories.aws.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.photomemories.aws.AwsFileServices;
import com.photomemories.domain.persistence.AwsBucket;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class AwsFileServiceImpl implements AwsFileServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsFileServiceImpl.class);

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
            throw new IllegalStateException("Could not download from the cloud", e);
        }
    }

    @Override
    public void deletePhotoFromFolder(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(AwsBucket.PROFILE_IMAGE.getAwsBucket(), fileName);
        s3.deleteObject(deleteObjectRequest);
    }

    //TODO: Update photo method

    @Override
    public ObjectListing getAllPhotos(String folderName) {
        ObjectListing objectListing = s3.listObjects(AwsBucket.PROFILE_IMAGE.getAwsBucket(), folderName + "/");
        if (objectListing != null) {
            List<S3ObjectSummary> s3ObjectSummariesList = objectListing.getObjectSummaries();
            if (!s3ObjectSummariesList.isEmpty()) {
                for (S3ObjectSummary objectSummary : s3ObjectSummariesList) {
                    System.out.println("file name:"+objectSummary.getKey());
                }
                return objectListing;
            }
        }
        return objectListing;
    }
}
