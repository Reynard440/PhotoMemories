package com.photomemories.logic;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AwsCRUDService {
    void uploadToS3(String email, MultipartFile file) throws IOException;

    byte[] downloadPhoto(String email, String imageName);

    String deletePhoto(String fileName, String email);

    String deleteFolderForUser(String email);

    String sharePhoto(String bucketName, String toBucketName, String key);
}
