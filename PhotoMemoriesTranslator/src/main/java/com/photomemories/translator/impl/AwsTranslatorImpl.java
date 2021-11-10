package com.photomemories.translator.impl;

import com.amazonaws.services.account.model.AWSAccountException;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.photomemories.aws.AwsFileServices;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.translator.AwsTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
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
            LOGGER.info("[AWS Translator log] save method, successful save");
        } catch (RuntimeException error) {
            LOGGER.warn("[AWS Translator log] save method, Could not execute the save request with error {}", error.getMessage());
            throw new RuntimeException("Could not execute the save request", error);
        }
    }

    @Override
    public byte[] download(String path, String key) {
        try {
            LOGGER.info("[AWS Translator log] download method, location {}{} ", path, key);
            byte[] downloadResult = awsFileServices.download(path, key);
            LOGGER.info("[AWS Translator log] download method, successful download");
            return downloadResult;
        } catch (AWSAccountException e) {
            LOGGER.warn("[AWS Translator log] download method, Could not execute the download request with error {}", e.getMessage());
            throw new RuntimeException("Could not download the photo", e.getCause());
        }
    }

    @Override
    public boolean deletePhotoFromFolder(String fileName) {
        try {
            awsFileServices.deletePhotoFromFolder(AwsBucket.PROFILE_IMAGE.getAwsBucket(), fileName);
            LOGGER.info("[AWS Translator log] deletePhotoFromFolder method, successfully deleted photo from {}", fileName);
            return true;
        } catch (RuntimeException e) {
            LOGGER.warn("[AWS Translator log] deletePhotoFromFolder method, Could not delete the photo with error {}", e.getMessage());
            throw new RuntimeException("Could not delete the photo", e);
        }
    }

    @Override
    public boolean deleteUserFolder(String path) {
        try {
            awsFileServices.deleteUserFolder(AwsBucket.PROFILE_IMAGE.getAwsBucket(), path);
            LOGGER.info("[AWS Translator log] deleteUserFolder method, successfully deleted folder from {}", path);
            return true;
        } catch (RuntimeException e) {
            LOGGER.error("[AWS Translator log] deleteUserFolder method, Could not delete the folder with error {}", e.getMessage());
            throw new RuntimeException("[AWS Translator Error] deleteUserFolder method, Could not delete the folder", e);
        }
    }

    @Override
    public boolean sharePhoto(String fromBucketName, String toBucketName, String key) {
        try {
            awsFileServices.sharePhoto(fromBucketName, toBucketName, key);
            LOGGER.info("[AWS Translator log] sharePhoto method, successfully shared photo from {}", key);
            return true;
        } catch (RuntimeException e) {
            LOGGER.error("[AWS Translator log] sharePhoto method, Could not share the photo with error {}", e.getMessage());
            throw new RuntimeException("[AWS Translator Error] sharePhoto method, Could not share the photo", e);
        }
    }

    //TODO: Update photo method

    @Override
    public ObjectListing getAllUserPhotos(String folderName) {
        LOGGER.info("Bucket name {} {} {}", AwsBucket.PROFILE_IMAGE.getAwsBucket(), folderName, "/");
        return awsFileServices.getAllPhotos(folderName);
    }

    @Override
    public ListObjectsRequest getAllPhotos(String bucketName, String folderName) {
        return awsFileServices.listAllPhotos(bucketName, folderName);
    }

    @Override
    public List listPhotos(String bucketName, String folderName) {
        return awsFileServices.listPhotos(bucketName, folderName);
    }

    @Override
    public byte[] getAllPhotosForUser(String path, String key) {
        return awsFileServices.getAllPhotosForUser(path, key);
    }
}
