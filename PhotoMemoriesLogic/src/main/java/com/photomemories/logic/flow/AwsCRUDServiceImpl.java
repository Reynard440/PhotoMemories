package com.photomemories.logic.flow;

import com.amazonaws.services.s3.model.ObjectListing;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.translator.AwsTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.http.entity.ContentType.*;

@Component("awsServiceFlow")
public class AwsCRUDServiceImpl implements AwsCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsCRUDServiceImpl.class);
    private final UserTranslator userTranslator;
    private final AwsTranslator awsTranslator;

    @Autowired
    public AwsCRUDServiceImpl(UserTranslator userTranslator, AwsTranslator awsTranslator) {
        this.userTranslator = userTranslator;
        this.awsTranslator = awsTranslator;
    }

    @Transactional(rollbackOn = {IllegalStateException.class, RuntimeException.class})
    @Override
    public void uploadToS3(String email, MultipartFile photo) throws IOException {
        try {
            isPhotoEmpty(photo);

            isPhoto(photo);

            Map<String, String> extraData = getMetadata(photo);

            UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));

            LOGGER.info("File name {}", photo.getOriginalFilename());

            String path = String.format("%s/%s", AwsBucket.PROFILE_IMAGE.getAwsBucket(), userDto.getUserId());
            String filename = photo.getOriginalFilename();
            awsTranslator.save(path, filename, Optional.of(extraData), photo.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] downloadPhoto(String email, String imageName){
        UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
        String path = String.format("%s",
                AwsBucket.PROFILE_IMAGE.getAwsBucket());
        String key = String.format("%s/%s", userDto.getUserId(), imageName);
        LOGGER.info("The path and key is {}/{}", path, key);
        return awsTranslator.download(path, key);
    }

    @Override
    public String deletePhoto(String fileName, String email) {
        UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
        awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName);
        if (!awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName)) {
            return "Photo could not be deleted";
        }
        return "Photo was deleted successfully";
    }

    //TODO: Update photo method

    @Override
    public ObjectListing getAllPhotosOfUser(String folderName) {
        return awsTranslator.getAllUserPhotos(folderName);
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
