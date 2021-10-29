package com.photomemories.aws;

import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AwsFileServices {
    void save(String path, String fileName, Optional<Map<String,String>> optionalMetaData, InputStream inputStream);

    byte[] download(String path, String key);

    //TODO: Delete photo method

    //TODO: Update photo method

    List<Object> getAllPhotos (Integer id);
}
