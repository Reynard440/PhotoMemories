package com.photomemories.aws;

import com.amazonaws.services.s3.model.ObjectListing;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AwsFileServices {
    void save(String path, String fileName, Optional<Map<String,String>> optionalMetaData, InputStream inputStream);

    byte[] download(String path, String key);

    void deletePhotoFromFolder(String fileName);

    //TODO: Update photo method

    ObjectListing getAllPhotos (String folderName);
}
