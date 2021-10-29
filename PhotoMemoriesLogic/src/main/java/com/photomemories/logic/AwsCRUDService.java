package com.photomemories.logic;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AwsCRUDService {
    void uploadToS3(Integer id, MultipartFile file);

    byte[] downloadPhoto(Integer id, String imageName);

    //TODO: Delete photo method

    //TODO: Update photo method

    List<Object> getAllPhotosOfUser(Integer id);
}
