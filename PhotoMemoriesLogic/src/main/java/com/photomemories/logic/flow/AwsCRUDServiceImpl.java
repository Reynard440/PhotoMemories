package com.photomemories.logic.flow;

import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.domain.persistence.Photo;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.translator.AwsTranslator;
import com.photomemories.translator.PhotoTranslator;
import com.photomemories.translator.SharedTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Component("awsServiceFlow")
public class AwsCRUDServiceImpl implements AwsCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsCRUDServiceImpl.class);
    private final SharedTranslator sharedTranslator;
    private final PhotoTranslator photoTranslator;
    private final UserTranslator userTranslator;
    private final AwsTranslator awsTranslator;

    @Autowired
    public AwsCRUDServiceImpl(SharedTranslator sharedTranslator, PhotoTranslator photoTranslator, UserTranslator userTranslator, AwsTranslator awsTranslator) {
        this.sharedTranslator = sharedTranslator;
        this.photoTranslator = photoTranslator;
        this.userTranslator = userTranslator;
        this.awsTranslator = awsTranslator;
    }

    @Override
    public void uploadPhoto(Integer id, MultipartFile photo){
        isPhotoEmpty(photo);

        isPhoto(photo);

        Map<String, String> metadata = extractMetadata(photo);

        Shared shared = sharedTranslator.getSharedByUserId(id);
        User user = userTranslator.getUserById(shared.getUserId().getUserId());
        Photo p = photoTranslator.getPhotoById(shared.getPhotoId().getPhotoId());

        String path = String.format("%s/%s", AwsBucket.PROFILE_IMAGE.getAwsBucket(), user.getUserId());
        String filename = String.format("%s-%s", photo.getOriginalFilename(), user.getUserId());
        try {
            awsTranslator.save(path, filename, Optional.of(metadata), photo.getInputStream());
            p.setPhotoLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    //TODO: convert from application-octet/stream to the actual image
    @Override
    public byte[] downloadPhoto(Integer id, String imageName){
        Shared shared = sharedTranslator.getSharedByUserId(id);
        User user = userTranslator.getUserById(shared.getUserId().getUserId());
        Photo photo = photoTranslator.getPhotoById(shared.getPhotoId().getPhotoId());

        String path = String.format("%s",
                AwsBucket.PROFILE_IMAGE.getAwsBucket());
        String key = String.format("%s/%s", user.getUserId(), photo.getPhotoLink());
        LOGGER.info("The path and key is {}/{}", path, key);
        return awsTranslator.download(path, key);
    }

    @Override
    public List<Object> getAllPhotosOfUser(Integer id) {
        return awsTranslator.getAllUserPhotos(id);
    }

    private void isPhotoEmpty(MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + photo.getSize() + " ]");
        }
    }

    private void isPhoto(MultipartFile photo) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_TIFF.getMimeType(),
                IMAGE_WEBP.getMimeType()).contains(photo.getContentType())) {
            throw new IllegalStateException("File must be an image [" + photo.getContentType() + "]");
        }
    }

    private Map<String, String> extractMetadata(MultipartFile photo) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", photo.getContentType());
        metadata.put("Content-Length", String.valueOf(photo.getSize()));
        return metadata;
    }
}
