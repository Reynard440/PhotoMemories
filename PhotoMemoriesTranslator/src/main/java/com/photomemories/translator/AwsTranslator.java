package com.photomemories.translator;

import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public interface AwsTranslator {
    void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream);

    byte[] download(String path, String key);

    boolean deletePhotoFromFolder(String fileName);

    boolean deleteUserFolder(String path);

    //TODO: Update photo method

    ObjectListing getAllUserPhotos(String folderName);

    ListObjectsRequest getAllPhotos(String bucketName, String folderName);

    List listPhotos(String bucketName, String folderName);

    byte[] getAllPhotosForUser(String path, String key);
}
