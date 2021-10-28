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

import javax.transaction.Transactional;
import java.sql.SQLException;
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

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
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

            Shared shared = sharedDto.buildShared();

            Shared addedShared = sharedTranslator.addShared(shared);

            SharedDto returnShared = new SharedDto(sharedTranslator.addShared(addedShared));

            return returnShared;
        } catch (Exception e) {
            throw new RuntimeException("Could not complete the action", e);
        }
    }

    @Override
    public SharedDto getSharedByUserId(Integer id) {
        return new SharedDto(sharedTranslator.getSharedByUserId(id));
    }
}
