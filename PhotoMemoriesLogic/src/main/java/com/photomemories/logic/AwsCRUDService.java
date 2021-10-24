package com.photomemories.logic;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AwsCRUDService {
    void uploadPhoto(Integer id, MultipartFile file);

    byte[] downloadPhoto(Integer id);
}
