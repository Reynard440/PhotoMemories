package com.photomemories.logic;

import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AwsCRUDService {
    void uploadToS3(String email, MultipartFile file) throws IOException;

    byte[] downloadPhoto(String email, String imageName);

    String deletePhoto(String fileName, String email);

    //TODO: Update photo method

    ObjectListing getAllPhotosOfUser(String folderName);
}
