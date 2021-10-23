package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.Shared;
import com.photomemories.repo.persistence.SharedRepository;
import com.photomemories.translator.SharedTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SharedTranslatorImpl implements SharedTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedTranslatorImpl.class);
    private final SharedRepository sharedRepository;

    @Autowired
    public SharedTranslatorImpl (SharedRepository sharedRepository) {
        this.sharedRepository = sharedRepository;
    }

    @Override
    public Shared addShared(Shared shared) throws Exception {
        LOGGER.info("[Shared Translator log] addShared method, input object: {}", shared);
        return sharedRepository.save(shared);
    }

    //TODO: Delete methods (must cascade from parent entities)
    //TODO: Update methods for sh_has_access and sh_shared_with columns
}
