package com.photomemories.logic;

import org.springframework.web.multipart.MultipartFile;

public interface AwsCRUDService {
    void uploadToS3(String email, MultipartFile file) throws RuntimeException;

    byte[] downloadPhoto(String email, String imageName) throws Exception;

    String deletePhoto(String fileName, String email);

    String deleteFolderForUser(String email);

    String sharePhoto(String bucketName, String toBucketName, String key);
}
