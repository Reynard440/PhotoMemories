package com.photomemories.aws.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.photomemories.aws.AwsFileServices;
import com.photomemories.domain.persistence.AwsBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public ListObjectsRequest listAllPhotos(String bucketName, String folderName) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
                .withPrefix(folderName);
        return listObjectsRequest;
    }

    @Override
    public List listPhotos(String bucketName, String folderName) {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName)
                        .withPrefix(folderName + "/");

        List keys = new ArrayList<>();

        ObjectListing objects = s3.listObjects(listObjectsRequest);

        while (true) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.size() < 1) {
                break;
            }

            for (S3ObjectSummary item : summaries) {
                if (!item.getKey().endsWith("/")) {
                    keys.add(item.getKey());
                }
            }

            objects = s3.listNextBatchOfObjects(objects);
        }

        return keys;
    }

    @Override
    public byte[] getAllPhotosForUser(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download", e);
        }
    }


}
