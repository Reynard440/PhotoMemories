package com.photomemories.logic.flow;

import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.translator.PhotoTranslator;
import com.photomemories.translator.SharedTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("sharedServiceFlow")
public class SharedCRUDServiceImpl implements SharedCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedCRUDServiceImpl.class);
    private final SharedTranslator sharedTranslator;
    private final UserTranslator userTranslator;
    private final PhotoTranslator photoTranslator;

    @Autowired
    public SharedCRUDServiceImpl (SharedTranslator sharedTranslator, UserTranslator userTranslator, PhotoTranslator photoTranslator) {
        this.sharedTranslator = sharedTranslator;
        this.userTranslator = userTranslator;
        this.photoTranslator = photoTranslator;
    }

    @Override
    public SharedDto createSharedDto(SharedDto sharedDto) throws Exception {
        try {
            if (!userTranslator.userExists(sharedDto.getUserId())) {
                LOGGER.warn("[Shared Logic log] input Dto contained invalid user id: {}", "invalid");
                sharedDto.setUserId(0);
            }
            if (!photoTranslator.photoExists(sharedDto.getPhotoId())) {
                LOGGER.warn("[Shared Logic log] input Dto contained invalid photo id: {}", "invalid");
                sharedDto.setSharedId(0);
            }
            LOGGER.info("[Shared Logic log] input Dto object is: {}", sharedDto);
            Shared shared = sharedDto.buildShared();
            LOGGER.info("[Shared Logic log] Dto Object converted to persistence is: {}", shared);
            Shared addedShared = sharedTranslator.addShared(shared);
            LOGGER.info("[Shared Logic log] object saved to database: {}", addedShared);
            SharedDto returnShared = new SharedDto(sharedTranslator.addShared(addedShared));
            LOGGER.info("[Shared Logic log] Dto returned: {}", returnShared);
            return returnShared;
        } catch (Exception e) {
            throw new RuntimeException("Shared record could not be created!", e);
        }
    }
}
