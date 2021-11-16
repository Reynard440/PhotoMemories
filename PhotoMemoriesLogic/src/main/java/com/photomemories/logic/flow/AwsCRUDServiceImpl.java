package com.photomemories.logic.flow;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.translator.AwsTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

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

            LOGGER.info("[AWS Logic log] uploadToS3 method, saved successfully");
        } catch (IOException e) {
            LOGGER.warn("[AWS Logic log] uploadToS3 method, exception {} ", e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] downloadPhoto(String email, String imageName){
        UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
        String path = String.format("%s", AwsBucket.PROFILE_IMAGE.getAwsBucket());
        String key = String.format("%s/%s", userDto.getUserId(), imageName);
        LOGGER.info("The path and key is {}/{}", path, key);
        byte[] downloadValue = awsTranslator.download(path, key);
        LOGGER.info("[AWS Logic log] downloadPhoto method, Photo downloaded successfully");
        return downloadValue;
    }

    @Override
    public String deletePhoto(String fileName, String email) {
        UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
        awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName);
        if (!awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName)) {
            LOGGER.warn("[AWS Logic log] deletePhoto method, Photo could not be deleted");
            return "Photo could not be deleted";
        }
        LOGGER.info("[AWS Logic log] deletePhoto method, Photo was deleted successfully");
        return "Photo could not be deleted";
    }

    @Override
    public String deleteFolderForUser(String email) {
        UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
        awsTranslator.deleteUserFolder("/" + userDto.getUserId());
        if (!awsTranslator.deleteUserFolder("/" + userDto.getUserId())) {
            LOGGER.warn("[AWS Logic log] deleteFolderForUser method, Folder could not be deleted");
            return "Folder not deleted";
        }
        LOGGER.info("[AWS Logic log] deleteFolderForUser method, Folder was deleted successfully");
        return "Folder deleted successfully";
    }

    @Override
    public String sharePhoto(String fromBucketName, String toBucketName, String key) {
        awsTranslator.sharePhoto(fromBucketName, toBucketName, key);
        return "Success";
    }

    private void isPhotoEmpty(MultipartFile photo) {
        if (photo.isEmpty()) {
            LOGGER.warn("[AWS Logic log] isPhotoEmpty method, No photo provided [ {} ]", photo.getSize());
            throw new IllegalStateException("No photo provided [ " + photo.getSize() + " ]");
        }
    }

    private void isPhoto(MultipartFile photo) {
        List<String> contentTypes = new ArrayList<>();
        contentTypes.add("image/png");
        contentTypes.add("image/jpeg");
        contentTypes.add("image/jpeg");
        contentTypes.add("image/jpg");
        contentTypes.add("image/bmp");
        contentTypes.add("image/ico");
        contentTypes.add("image/gif");
        contentTypes.add("image/tiff");
        if (!contentTypes.contains(photo.getContentType())) {
            LOGGER.warn("[AWS Logic log] isPhoto method, File type [ {} ] not allowed for uploading", photo.getContentType());
            throw new IllegalStateException("[AWS Logic Error] isPhoto method, File type [" + photo.getContentType() + "] not allowed for uploading");
        }
    }

    private Map<String, String> getMetadata(MultipartFile photo) {
        Map<String,String> data = new HashMap<>();
        data.put("Content-Type", photo.getContentType());
        LOGGER.info("The content-Type is {}", photo.getContentType());
        data.put("Content-Length", String.valueOf(photo.getSize()));
        LOGGER.info("The content-Length is {}", photo.getSize());
        return data;
    }
}
