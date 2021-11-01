package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.Shared;
import com.photomemories.repo.persistence.SharedRepository;
import com.photomemories.translator.SharedTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Component
public class SharedTranslatorImpl implements SharedTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedTranslatorImpl.class);
    private final SharedRepository sharedRepository;

    @Autowired
    public SharedTranslatorImpl (SharedRepository sharedRepository) {
        this.sharedRepository = sharedRepository;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Shared addShared(Shared shared) throws SQLException {
        LOGGER.info("[Shared Translator log] addShared method, input object's date: {}", shared.getSharedDate());
        return sharedRepository.save(shared);
    }

    @Override
    public Shared getSharedByUserId(Integer id) {
        LOGGER.info("[Shared Translator log] getSharedByUserId method, input id: {}", id);
        return sharedRepository.findByUserId_UserId(id);
    }

    @Override
    public Shared sharePhoto(Shared sharePhoto) throws Exception {
        LOGGER.info("[Shared Translator log] sharePhoto method, input object's date: {}", sharePhoto.getSharedDate());
        return sharedRepository.save(sharePhoto);
    }

    //TODO: Delete methods (must cascade from parent entities)
}
