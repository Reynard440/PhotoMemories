package com.photomemories.logic.flow;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.photomemories.domain.dto.PhotoDto;
import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.AwsBucket;
import com.photomemories.domain.persistence.Photo;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.logic.PhotoCRUDService;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.logic.UserCRUDService;
import com.photomemories.translator.PhotoTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("photoServiceFlow")
public class PhotoCRUDServiceImpl implements PhotoCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoCRUDServiceImpl.class);
    private final PhotoTranslator photoTranslator;
    private final UserCRUDService userCRUDService;
    private final SharedCRUDService sharedCRUDService;
    private final AwsCRUDService awsCRUDService;

    //Dependency Injection: photoTranslator, userCRUDService, sharedCRUDService, and awsCRUDService are injected to ensure singleton pattern is followed
    @Autowired
    public PhotoCRUDServiceImpl(PhotoTranslator photoTranslator, AwsCRUDService awsCRUDService, UserCRUDService userCRUDService, SharedCRUDService sharedCRUDService) {
        this.photoTranslator = photoTranslator;
        this.userCRUDService = userCRUDService;
        this.sharedCRUDService = sharedCRUDService;
        this.awsCRUDService = awsCRUDService;
    }

    //Inserts a photo in the database and on AWS
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public PhotoDto createPhotoDto(PhotoDto photoDto, String email, MultipartFile photoFile) throws Exception {
        try {
            UserDto user = userCRUDService.getUserDtoByEmail(email);
            Photo photo = photoDto.buildPhoto();
            LOGGER.info("[Photo Logic log] createPhotoDto method, Dto Object converted to persistence is: {}", photo);

            photo.setUploadDate(LocalDate.now());
            photo.setDateModified(LocalDate.now());

            Photo addedPhoto = photoTranslator.addPhoto(photo);
            LOGGER.info("[Photo Logic log] createPhotoDto method, object saved to database: {}", addedPhoto);

            awsCRUDService.uploadToS3(email, photoFile);

            SharedDto sharedDto = new SharedDto();
            sharedDto.setSharedHasAccess(true);
            sharedDto.setPhotoId(addedPhoto.getPhotoId());
            sharedDto.setSharedDate(LocalDate.now());
            sharedDto.setUserId(user.getUserId());
            sharedDto.setSharedWith(user.getUserId());
            SharedDto dto = sharedCRUDService.createSharedDto(sharedDto);

            PhotoDto returnDto = new PhotoDto(photoTranslator.addPhoto(addedPhoto));
            LOGGER.info("[Photo Logic log] createPhotoDto method, Dto returned: {}", returnDto);

            return returnDto;
        } catch (RuntimeException e) {
            LOGGER.error("[Photo Logic log] createPhotoDto method, Photo could not be created, with error {}", e.getMessage());
            throw new RuntimeException("[Photo Logic Error] createPhotoDto method, Request could not be executed ", e);
        }
    }

    //Deletes a photo in the database and from AWS by given id and photoLink
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer deletePhotoDto(Integer id, String photoLink, String email) throws SQLException, Exception {
        try {
            boolean beforeDelete = photoTranslator.photoExists(id, photoLink);
            LOGGER.info("[Photo Logic log] deletePhoto method, (exists?): {}", beforeDelete);

            if (!photoExists(id, photoLink)) {
                LOGGER.error("[Photo Logic log] deletePhoto method, Photo with id {} does not exists", id);
                throw new RuntimeException("[Photo Logic Error] deletePhoto method, Photo with id " + id + " does not exist!");
            }

            int photoDelete = photoTranslator.deletePhoto(id, photoLink);
            awsCRUDService.deletePhoto(photoLink, email);
            boolean afterDelete = photoTranslator.photoExists(id, photoLink);
            LOGGER.info("[Photo Logic log] deletePhoto method, (exists?): {}", afterDelete);

            return photoDelete;
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] createPhotoDto method, Photo could not be delete, with error {}", error.getMessage());
            throw new SQLException("[Photo Logic Error] createPhotoDto method, Request could not be executed ", error.getCause());
        }
    }

    //Updates the metadata of a photo in the database with given information
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public PhotoDto updatePhotoDto(String pName, String pLocation, String pCapturedBy, Integer photoId, String email) throws SQLException {
        try {
            int returnValue = photoTranslator.updatePhoto(pName, pLocation, pCapturedBy, photoId);

            if (returnValue == 0) {
                LOGGER.error("[Photo Logic log] updatePhotoDto method, did not update photo metadata: {}", false);
                throw new RuntimeException("[Photo Logic Error] updatePhotoDto method, did not update metadata");
            }

            return new PhotoDto(photoTranslator.getPhotoById(photoId));
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] updatePhotoDto method, Photo could not be updated, with error {}", error.getMessage());
            throw new SQLException("[Photo Logic Error] updatePhotoDto method, Request could not be executed ", error.getCause());
        }
    }

    //Returns a photoDto object from the database with given photo id
    @Override
    public PhotoDto getPhotoDtoById(Integer id) {
        try {
            LOGGER.info("[Photo Logic log] getPhotoDtoById method, input id {}", id);
            return new PhotoDto(photoTranslator.getPhotoById(id));
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] getPhotoDtoById method, Photo could not be found with id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[Photo Logic Error] getPhotoDtoById method, Request could not be executed ", error.getCause());
        }
    }

    //Shares a specified photo with another user in the database and on AWS
    @Override
    public String sendPhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer photoId) {
        try {
            if (!userCRUDService.userExistsByEmail(receivingEmail)) {
                LOGGER.error("[Photo Logic log] sendPhoto method, Could not share the photo with email {}", receivingEmail);
                throw new UserNotFoundException("[Photo Logic Error] sendPhoto method");
            }
            UserDto receivingUserDto = userCRUDService.getUserDtoByEmail(receivingEmail);
            UserDto sendUserDto = userCRUDService.getUserDtoByEmail(sharingEmail);
            PhotoDto photoDto = new PhotoDto(photoTranslator.getPhotoById(photoId));

            SharedDto sharedDto = new SharedDto(LocalDate.now(), receivingUserDto.getUserId(), accessRights, sendUserDto.getUserId(), photoId);
            Shared shared = sharedDto.buildShared();
            SharedDto addedSharePhoto = sharedCRUDService.createSharedDto(sharedDto);
            String fromBucket = AwsBucket.PROFILE_IMAGE.getAwsBucket() + "/" + sendUserDto.getUserId();
            String toBucket = AwsBucket.PROFILE_IMAGE.getAwsBucket() + "/" + receivingUserDto.getUserId();

            awsCRUDService.sharePhoto(fromBucket, toBucket, photoDto.getPhotoLink());

            LOGGER.info("[Photo Logic log] sendPhoto method, shared the photo with email {}", receivingEmail);
            return "Photo shared";
        } catch (Exception error) {
            LOGGER.error("[Photo Logic log] sendPhoto method, Could not share the photo with email {}, with error {}", receivingEmail, error.getMessage());
            throw new RuntimeException("[Photo Logic Error] sendPhoto method, Request could not be executed ", error.getCause());
        }
    }

    //Loads all the photos from the database by a give user email
    @Override
    public List<PhotoDto> getPhotosByUserEmail(String email) {
        try {
            LOGGER.info("[Photo Logic log] getPhotosByUserEmail method, email {}", email);
            UserDto userDto = userCRUDService.getUserDtoByEmail(email);
            LOGGER.info("[Photo Logic log] getPhotosByUserEmail method, id {}", userDto.getUserId());
            return photoTranslator.findByUserEmail(userDto.getUserId()).stream().map(PhotoDto::new).collect(Collectors.toList());
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] getPhotosByUserEmail method, Could not return a list of photos for t=user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[Photo Logic Error] getPhotosByUserEmail method, Request could not be executed ", error.getCause());
        }
    }

    //Loads all the photos from the database
    @Override
    public List<PhotoDto> getAllPhotos() {
        try {
            List<PhotoDto> photos = new ArrayList<>();
            for(Photo photo : photoTranslator.getAllPhotos()) {
                photos.add(new PhotoDto(photo));
            }
            return photos;
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] getAllPhotos method, Could not get all photos, with error {}", error.getMessage());
            throw new RuntimeException("[Photo Logic Error] getAllPhotos method, Request could not be executed ", error.getCause());
        }
    }

    //Checks if a photo exists with specified id and photoLink in the database
    @Override
    public boolean photoExists(Integer id, String photoLink) {
        try {
            boolean returnPhotoLogicValue = photoTranslator.photoExists(id, photoLink);
            LOGGER.info("[Photo Logic log] photoExists method, result {}", returnPhotoLogicValue);
            return returnPhotoLogicValue;
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Logic log] photoExists method, Photo with id {} and link {} could not be found, with error {}", id, photoLink, error.getMessage());
            throw new RuntimeException("[Photo Logic Error] photoExists method, Request could not be executed ", error.getCause());
        }
    }
}
