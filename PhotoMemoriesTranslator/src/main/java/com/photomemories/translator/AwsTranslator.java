package com.photomemories.translator;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Component
public interface AwsTranslator {
    void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream);

    byte[] download(String path, String key);

    boolean deletePhotoFromFolder(String fileName);

    boolean deleteUserFolder(String path);

    boolean sharePhoto(String bucketName, String toBucketName, String key);
}
