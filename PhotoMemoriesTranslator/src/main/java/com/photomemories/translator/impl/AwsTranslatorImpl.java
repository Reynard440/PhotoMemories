package com.photomemories.translator.impl;

import com.amazonaws.services.s3.model.ObjectListing;
import com.photomemories.aws.AwsFileServices;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.translator.AwsTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Component
public class AwsTranslatorImpl implements AwsTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsTranslatorImpl.class);

    private final AwsFileServices awsFileServices;

    @Autowired
    public AwsTranslatorImpl(AwsFileServices awsFileServices) {
        this.awsFileServices = awsFileServices;
    }

    @Override
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        try {
            awsFileServices.save(path, fileName, optionalMetaData, inputStream);
        } catch (RuntimeException error) {
            throw new RuntimeException("Could not execute the save request", error);
        }
    }

    @Override
    public byte[] download(String path, String key) {
        try {
             return awsFileServices.download(path, key);
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not download the photo", e);
        }
    }

    @Override
    public boolean deletePhotoFromFolder(String fileName) {
        try {
            awsFileServices.deletePhotoFromFolder(fileName);
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not delete the photo", e);
        }
        return false;
    }

    //TODO: Update photo method

    @Override
    public ObjectListing getAllUserPhotos(String folderName) {
        LOGGER.info("Bucket name {} {} {}", AwsBucket.PROFILE_IMAGE.getAwsBucket(), folderName, "/");
        return awsFileServices.getAllPhotos(folderName);
    }
}
