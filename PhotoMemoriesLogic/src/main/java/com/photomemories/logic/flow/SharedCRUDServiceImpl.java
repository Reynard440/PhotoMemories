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
    public SharedDto createSharedDto(SharedDto sharedDto) throws Exception {
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

    @Override
    public SharedDto getSharedByUserId(Integer id) {
        LOGGER.info("[Shared Logic log] getSharedByUserId method, input id {}", id);
        return new SharedDto(sharedTranslator.getSharedByUserId(id));
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public String sharePhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer id, MultipartFile photo) throws Exception {
        try {
            if (!userTranslator.userExistsWithEmail(receivingEmail)) {
                LOGGER.error("[Shared Logic log] sharePhoto method, Could not share the photo with email {}", receivingEmail);
                throw new UserNotFoundException("[Shared Logic Error] sharePhoto method");
            }
            UserDto receivingUserDto = new UserDto(userTranslator.getUserByEmail(receivingEmail));
            UserDto sendUserDto = new UserDto(userTranslator.getUserByEmail(sharingEmail));

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

//    @Override
//    public SharedDto findBySharedDtoVerifiedIds(Integer sharedWitdId, Integer userId, Integer photoId) {
//        LOGGER.info("[Shared Logic log] findBySharedDtoVerifiedIds method, photo id {} shared With Id {} by userId {}", userId, userId, photoId);
//        SharedDto verifyAccess = new SharedDto(sharedTranslator.findBySharedVerifiedIds(sharedWitdId, userId, photoId));
//        if (verifyAccess == null) {
//            LOGGER.error("[Shared Logic log] findBySharedDtoVerifiedIds method, no such record exists");
//            throw new RuntimeException("[Shared Logic log] findBySharedDtoVerifiedIds method, no such record exists");
//        }
//        return verifyAccess;
//    }
}
