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

    @Autowired
    public PhotoCRUDServiceImpl(PhotoTranslator photoTranslator, AwsCRUDService awsCRUDService, UserCRUDService userCRUDService, SharedCRUDService sharedCRUDService) {
        this.photoTranslator = photoTranslator;
        this.userCRUDService = userCRUDService;
        this.sharedCRUDService = sharedCRUDService;
        this.awsCRUDService = awsCRUDService;
    }

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
        } catch (Exception e) {
            LOGGER.error("[Photo Logic log] createPhotoDto method, Photo could not be created with error {}", e.getMessage());
            throw new SQLException("[Photo Logic Error] createPhotoDto method, Photo could not be created!", e);
        }
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer deletePhotoDto(Integer id, String photoLink, String email) throws Exception {
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
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public PhotoDto updatePhotoDto(String pName, String pLocation, String pCapturedBy, Integer photoId, String email) {
        int returnValue = photoTranslator.updatePhoto(pName, pLocation, pCapturedBy, photoId);

        if (returnValue == 0) {
            LOGGER.error("[Photo Logic log] updatePhotoDto method, did not update photo metadata: {}", false);
            throw new RuntimeException("[Photo Logic Error] updatePhotoDto method, did not update metadata");
        }

        return new PhotoDto(photoTranslator.getPhotoById(photoId));
    }

    @Override
    public PhotoDto getPhotoDtoById(Integer id) {
        LOGGER.info("[Photo Logic log] getPhotoDtoById method, input id {}", id);
        return new PhotoDto(photoTranslator.getPhotoById(id));
    }

    @Override
    public List<PhotoDto> getAllPhotoDtosOfUser(Integer userId) {
        LOGGER.info("[Photo Logic log] getAllPhotoDtosOfUser method, user id {}", userId);
        List<PhotoDto> list = new ArrayList<>();
        Integer index = 0;
        for (Photo photo : photoTranslator.getAllPhotosOfUser(userId)) {
            PhotoDto photoDto = new PhotoDto(photo);
            list.add(photoDto);
            list.stream().distinct();
        }
        return list;
    }

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
        } catch (Exception e) {
            LOGGER.error("[Photo Logic log] sendPhoto method, Could not share the photo with email {}", receivingEmail);
            throw new RuntimeException("Exception with error ", e.getCause());
        }
    }

    @Override
    public PhotoDto getByPhotoNameAndPhotoFormat(String name, String format) {
        LOGGER.info("[Photo Logic log] getByPhotoNameAndPhotoFormat method, input name {} and format {}", name, format);
        return new PhotoDto(photoTranslator.findByPhotoNameAndPhotoFormat(name, format));
    }

    @Override
    public List<PhotoDto> getPhotosByUserEmail(String email) {
        LOGGER.info("[Photo Logic log] getPhotosByUserEmail method, email {}", email);
        UserDto userDto = userCRUDService.getUserDtoByEmail(email);
        LOGGER.info("[Photo Logic log] getPhotosByUserEmail method, id {}", userDto.getUserId());
        return photoTranslator.findByUserEmail(userDto.getUserId()).stream().map(PhotoDto::new).collect(Collectors.toList());
    }

    @Override
    public List<PhotoDto> getAllPhotos() {
        List<PhotoDto> photos = new ArrayList<>();
        for(Photo photo : photoTranslator.getAllPhotos()) {
            photos.add(new PhotoDto(photo));
        }
        return photos;
    }

    @Override
    public boolean photoExists(Integer id, String photoLink) {
        boolean returnPhotoLogicValue = photoTranslator.photoExists(id, photoLink);
        LOGGER.info("[Photo Logic log] photoExists method, result {}", returnPhotoLogicValue);
        return returnPhotoLogicValue;
    }
}
