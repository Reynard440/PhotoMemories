package com.photomemories.logic.flow;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.translator.SharedTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDate;

@Component("sharedServiceFlow")
public class SharedCRUDServiceImpl implements SharedCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedCRUDServiceImpl.class);
    private final SharedTranslator sharedTranslator;
    private final UserTranslator userTranslator;
    private final AwsCRUDService awsCRUDService;

    @Autowired
    public SharedCRUDServiceImpl (SharedTranslator sharedTranslator, UserTranslator userTranslator, AwsCRUDService awsCRUDService) {
        this.sharedTranslator = sharedTranslator;
        this.userTranslator = userTranslator;
        this.awsCRUDService = awsCRUDService;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public SharedDto createSharedDto(SharedDto sharedDto) throws RuntimeException, SQLException, Exception {
        try {
            if (!userTranslator.userExists(sharedDto.getUserId())) {
                LOGGER.warn("[Shared Logic log] createSharedDto method, input Dto contained invalid user id: {}", "invalid");
                sharedDto.setUserId(0);
            }
            Shared shared = sharedDto.buildShared();
            Shared addedShared = sharedTranslator.addShared(shared);
            SharedDto returnShared = new SharedDto(sharedTranslator.addShared(addedShared));

            return returnShared;
        } catch (Exception e) {
            LOGGER.warn("[Shared Logic log] createSharedDto method, exception with error {}", e.getMessage());
            throw new RuntimeException("Could not complete the action", e);
        }
    }

    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Integer deleteBySharedRecord(Integer sharedWith, Integer photoId, String photoLink) throws SQLException {
        try {
            LOGGER.info("[Shared Logic log] deleteBySharedRecord method, delete photo {} for user {} ", sharedWith, photoId);
            int response = sharedTranslator.deleteBySharedRecord(sharedWith, photoId);
            UserDto userDto = new UserDto(userTranslator.getUserById(sharedWith));
            awsCRUDService.deletePhoto(photoLink, userDto.getEmail());
            return response;
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Logic log] deleteBySharedRecord method, delete request could not be carried out");
            throw new RuntimeException("[Shared Logic Error] deleteBySharedRecord method, deletion error ", error.getCause());
        }
    }

    @Override
    public SharedDto findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId) {
        LOGGER.info("[Shared Logic log] findBySharedWithAndPhotoId method, shared with id {} and photo id {}", sharedWith, photoId);
        return new SharedDto(sharedTranslator.findBySharedWithAndPhotoId(sharedWith, photoId));
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public String sharePhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer id, MultipartFile photo) throws Exception {
        try {
            if (!userTranslator.userExistsWithEmail(receivingEmail)) {
                LOGGER.error("[Shared Logic log] sharePhoto method, Could not share the photo with email {}", receivingEmail);
                throw new UserNotFoundException("[Shared Logic Error] sharePhoto method");
            }
            UserDto receivingUserDto = getUserDtoByEmail(receivingEmail);
            UserDto sendUserDto = getUserDtoByEmail(sharingEmail);

            SharedDto sharedDto = new SharedDto(LocalDate.now(), receivingUserDto.getUserId(), accessRights, sendUserDto.getUserId(), id);
            Shared shared = sharedDto.buildShared();
            Shared addedSharePhoto = sharedTranslator.sharePhoto(shared);
            SharedDto returnSharePhoto = new SharedDto(sharedTranslator.sharePhoto(addedSharePhoto));

            awsCRUDService.uploadToS3(receivingEmail, photo);

            LOGGER.info("[Shared Logic log] sharePhoto method, shared the photo with email {}", receivingEmail);
            return "Photo shared";
        } catch (Exception e) {
            LOGGER.error("[Shared Logic log] sharePhoto method, Could not share the photo with email {}", receivingEmail);
            throw new RuntimeException("Exception with error ", e.getCause());
        }
    }

    @Override
    public boolean checkBySharedWithAndPhotoId(String email, Integer photoId) throws RuntimeException, Exception, SQLException {
        UserDto userDto = getUserDtoByEmail(email);
        LOGGER.info("[Shared Logic log] checkBySharedWithAndPhotoId method, photo id {} and email {}", photoId, email);
        if (sharedTranslator.existsBySharedWithAndPhotoId(userDto.getUserId(), photoId)) {
            return true;
        } else {
            LOGGER.error("[Shared Logic log] checkBySharedWithAndPhotoId method, user {} already has photo with id  {}", email, photoId);
            throw new SQLException("[Shared Logic Error] checkBySharedWithAndPhotoId method, user already has this photo");
        }
    }

    @Override
    public boolean existsBySharedWithAndUserIdAndPhotoId(String email, Integer photoId) throws RuntimeException, Exception, SQLException  {
        UserDto userDto = getUserDtoByEmail(email);
        LOGGER.info("[Shared Logic log] existsBySharedWithAndUserIdAndPhotoId method, photo id {} and email {}", photoId, email);
        if (sharedTranslator.existsBySharedWithAndUserIdAndPhotoId(userDto.getUserId(), userDto.getUserId(), photoId)) {
            LOGGER.error("[Shared Logic log] existsBySharedWithAndUserIdAndPhotoId method, user {} already has photo with id  {}", email, photoId);
            throw new SQLException("[Shared Logic Error] existsBySharedWithAndUserIdAndPhotoId method, user already has this photo");
        }
        return false;
    }

    private UserDto getUserDtoByEmail(String email) {
        return new UserDto(userTranslator.getUserByEmail(email));
    }
}
