package com.photomemories.aws;

import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AwsFileServices {
    void save(String path, String fileName, Optional<Map<String,String>> optionalMetaData, InputStream inputStream);

    byte[] download(String path, String key);

    void deletePhotoFromFolder(String bucketName, String fileName);

    void deleteUserFolder(String bucketName, String path);

    void sharePhoto(String bucketName, String toBucketName, String key);

    //TODO: Update photo method

    ObjectListing getAllPhotos (String folderName);

    ListObjectsRequest listAllPhotos(String bucketName, String folderName);

    List listPhotos(String bucketName, String folderName);

    byte[] getAllPhotosForUser(String path, String key);
}
