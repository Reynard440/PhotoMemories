package com.photomemories.aws.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.photomemories.aws.AwsFileServices;
import com.photomemories.domain.persistence.AwsBucket;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Could not download from the cloud", e);
        }
    }

    @Override
    public List<Object> getAllPhotos(Integer id) {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(AwsBucket.PROFILE_IMAGE.getAwsBucket());
        ObjectListing objs = s3.listObjects(String.valueOf(listObjectsV2Request));
        return Collections.singletonList(objs);
    }
}
