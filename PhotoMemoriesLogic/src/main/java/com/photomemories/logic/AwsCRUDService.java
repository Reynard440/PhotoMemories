package com.photomemories.logic;

import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AwsCRUDService {
    void uploadToS3(String email, MultipartFile file) throws IOException;

    byte[] downloadPhoto(String email, String imageName);

    String deletePhoto(String fileName, String email);

    String deleteFolderForUser(String path, String email);

    //TODO: Update photo method

    ObjectListing getAllPhotosOfUser(String folderName);
}
