package com.photomemories.logic.flow;

import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.translator.SharedTranslator;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Component("sharedServiceFlow")
public class SharedCRUDServiceImpl implements SharedCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedCRUDServiceImpl.class);
    private final SharedTranslator sharedTranslator;
    private final UserTranslator userTranslator;

    @Autowired
    public SharedCRUDServiceImpl (SharedTranslator sharedTranslator, UserTranslator userTranslator) {
        this.sharedTranslator = sharedTranslator;
        this.userTranslator = userTranslator;
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
}
