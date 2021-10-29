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

import javax.transaction.Transactional;
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

    @Transactional(rollbackOn = {IllegalStateException.class, RuntimeException.class})
    @Override
    public void uploadToS3(Integer id, MultipartFile photo){
        isPhotoEmpty(photo);

        isPhoto(photo);

        Map<String, String> extraData = getMetadata(photo);

        Shared shared = sharedTranslator.getSharedByUserId(id);
        User user = userTranslator.getUserById(shared.getUserId().getUserId());
        Photo p = photoTranslator.getPhotoById(shared.getPhotoId().getPhotoId());

        LOGGER.info("File name {}", photo.getOriginalFilename());

        String path = String.format("%s/%s", AwsBucket.PROFILE_IMAGE.getAwsBucket(), user.getUserId());
        String filename = String.format("%s", photo.getOriginalFilename());
        try {
            awsTranslator.save(path, filename, Optional.of(extraData), photo.getInputStream());
            p.setPhotoLink(filename);
            p.setPhotoName(photo.getOriginalFilename());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] downloadPhoto(Integer id, String imageName){
        Shared shared = sharedTranslator.getSharedByUserId(id);
        User user = userTranslator.getUserById(shared.getUserId().getUserId());
        Photo photo = photoTranslator.getPhotoById(shared.getPhotoId().getPhotoId());

        String path = String.format("%s",
                AwsBucket.PROFILE_IMAGE.getAwsBucket());
        String key = String.format("%s/%s", user.getUserId(), imageName);
        LOGGER.info("The path and key is {}/{}", path, key);
        return awsTranslator.download(path, key);
    }

    //TODO: Delete photo method

    //TODO: Update photo method

    @Override
    public List<Object> getAllPhotosOfUser(Integer id) {
        return awsTranslator.getAllUserPhotos(id);
    }

    private void isPhotoEmpty(MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new IllegalStateException("No photo provided [ " + photo.getSize() + " ]");
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
            throw new IllegalStateException("Photo must be an image of type [" + photo.getContentType() + "]");
        }
    }

    private Map<String, String> getMetadata(MultipartFile photo) {
        Map<String,String> data = new HashMap<>();
        data.put("Content-Type", photo.getContentType());
        LOGGER.info("The content-Type is {}", photo.getContentType());
        data.put("Content-Length", String.valueOf(photo.getSize()));
        return data;
    }
}
