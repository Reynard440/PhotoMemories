package com.photomemories.translator.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.translator.AwsTranslator;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

@Component
public class AwsTranslatorImpl implements AwsTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsTranslatorImpl.class);

    private final AmazonS3 s3;

    @Autowired
    public AwsTranslatorImpl(AmazonS3 s3) {
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
            objectMetadata.setContentType(objectMetadata.getContentType());
            s3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException error) {
            throw new IllegalStateException("Failed to store the file to s3", error);
        }
    }

    @Override
    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download", e);
        }
    }

    @Override
    public List<Object> getAllUserPhotos(Integer id) {
        LOGGER.info("Bucket name {} {} {}", AwsBucket.PROFILE_IMAGE.getAwsBucket(), id, "/");
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(AwsBucket.PROFILE_IMAGE.getAwsBucket());
        ObjectListing objs = s3.listObjects(String.valueOf(listObjectsV2Request));
        return Collections.singletonList(objs);
    }
}
