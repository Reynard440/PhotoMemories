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

    //Dependency Injection: userTranslator and awsTranslator are injected to ensure singleton pattern is followed
    @Autowired
    public AwsCRUDServiceImpl(UserTranslator userTranslator, AwsTranslator awsTranslator) {
        this.userTranslator = userTranslator;
        this.awsTranslator = awsTranslator;
    }

    //Saving to the S3 bucket on AWS
    @Transactional(rollbackOn = {IllegalStateException.class, RuntimeException.class})
    @Override
    public void uploadToS3(String email, MultipartFile photo) throws RuntimeException {
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
        } catch (IOException error) {
            LOGGER.error("[AWS Logic log] uploadToS3 method, Could not add to AWS, with error {} ", error.getMessage());
            throw new IllegalStateException("[AWS Logic Error] uploadToS3 method, Could not execute the request", error.getCause());
        }
    }

    //Downloading from the S3 bucket on AWS
    @Override
    public byte[] downloadPhoto(String email, String imageName) throws Exception{
        try {
            UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
            String path = String.format("%s", AwsBucket.PROFILE_IMAGE.getAwsBucket());
            String key = String.format("%s/%s", userDto.getUserId(), imageName);
            LOGGER.info("The path and key is {}/{}", path, key);
            byte[] downloadValue = awsTranslator.download(path, key);
            LOGGER.info("[AWS Logic log] downloadPhoto method, Photo downloaded successfully");
            return downloadValue;
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Logic log] downloadPhoto method, Could not download from AWS, with error {}", error.getMessage());
            throw new RuntimeException("[AWS Logic Error] downloadPhoto method, Could not execute the request", error.getCause());
        }
    }

    //Deleting a photo from the S3 bucket on AWS
    @Override
    public String deletePhoto(String fileName, String email) {
        try {
            UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
            awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName);
            if (!awsTranslator.deletePhotoFromFolder(userDto.getUserId() + "/" + fileName)) {
                LOGGER.warn("[AWS Logic log] deletePhoto method, Photo could not be deleted");
                return "Photo could not be deleted";
            }
            LOGGER.info("[AWS Logic log] deletePhoto method, Photo was deleted successfully");
            return "Photo could not be deleted";
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Logic log] deletePhoto method, Could not delete the specified photo with name {}, with error {}", fileName, error.getMessage());
            throw new RuntimeException("[AWS Logic Error] deletePhoto method, Could not execute the request", error.getCause());
        }
    }

    //Deleting a folder from the S3 bucket on AWS
    @Override
    public String deleteFolderForUser(String email) {
        try {
            UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
            awsTranslator.deleteUserFolder("/" + userDto.getUserId());
            if (!awsTranslator.deleteUserFolder("/" + userDto.getUserId())) {
                LOGGER.warn("[AWS Logic log] deleteFolderForUser method, Folder could not be deleted");
                return "Folder not deleted";
            }
            LOGGER.info("[AWS Logic log] deleteFolderForUser method, Folder was deleted successfully");
            return "Folder deleted successfully";
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Logic log] deleteFolderForUser method, Could not delete the folder for user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[AWS Logic Error] deleteFolderForUser method, Could not execute the request", error.getCause());
        }
    }

    //Sharing a photo to another folder in the S3 bucket on AWS
    @Override
    public String sharePhoto(String fromBucketName, String toBucketName, String key) {
        try {
            awsTranslator.sharePhoto(fromBucketName, toBucketName, key);
            return "Success";
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Logic log] sharePhoto method, Could not share the specified photo, with error {}", error.getMessage());
            throw new RuntimeException("[AWS Logic Error] sharePhoto method, Could not execute the request", error.getCause());
        }
    }

    //Method that ensures that the given photo is not null
    private void isPhotoEmpty(MultipartFile photo) {
        if (photo.isEmpty()) {
            LOGGER.warn("[AWS Logic log] isPhotoEmpty method, No photo provided [ {} ]", photo.getSize());
            throw new IllegalStateException("No photo provided [ " + photo.getSize() + " ]");
        }
    }

    //Method that ensures that the given photo is of the correct format type
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

    //Method that maps the metadata of the actual photo
    private Map<String, String> getMetadata(MultipartFile photo) {
        Map<String,String> data = new HashMap<>();
        data.put("Content-Type", photo.getContentType());
        LOGGER.info("The content-Type is {}", photo.getContentType());
        data.put("Content-Length", String.valueOf(photo.getSize()));
        LOGGER.info("The content-Length is {}", photo.getSize());
        return data;
    }
}
