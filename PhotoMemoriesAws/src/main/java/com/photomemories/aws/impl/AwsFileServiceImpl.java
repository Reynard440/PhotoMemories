package com.photomemories.aws.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
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

    @Autowired
    public AwsFileServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {

    }

    @Override
    public byte[] download(String path, String key) {
        return new byte[0];
    }
}
