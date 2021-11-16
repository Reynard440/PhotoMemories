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

    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Shared addShared(Shared shared) throws RuntimeException, SQLException {
        LOGGER.info("[Shared Translator log] addShared method, input object's date {}", shared.getSharedDate());
        return sharedRepository.save(shared);
    }

    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Integer deleteBySharedRecord(Integer sharedWith, Integer photoId) throws SQLException {
        try {
            LOGGER.info("[Shared Translator log] deleteBySharedRecord method, delete photo {} for user {} ", sharedWith, photoId);
            return sharedRepository.deleteBySharedWithAndPhotoId_PhotoId(sharedWith, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] deleteBySharedRecord method, delete request could not be carried out");
            throw new RuntimeException("[Shared Translator Error] deleteBySharedRecord method, deletion error ", error.getCause());
        }
    }

    @Override
    public Shared getSharedBySharedId(Integer id) {
        LOGGER.info("[Shared Translator log] getSharedByUserId method, shared id {}", id);
        return sharedRepository.findBySharedId(id);
    }

    @Override
    public boolean existsBySharedWithAndPhotoId(Integer sharedWith, Integer photoId) {
        LOGGER.info("[Shared Translator log] existsBySharedWithAndPhotoId method, shared with id {} and photoId {} ", sharedWith, photoId);
        return sharedRepository.existsBySharedWithAndPhotoId_PhotoId(sharedWith, photoId);
    }

    @Override
    public boolean existsBySharedWithAndUserIdAndPhotoId(Integer sharedWith, Integer userId, Integer photoId) {
        LOGGER.info("[Shared Translator log] existsBySharedWithAndUserIdAndPhotoId method, shared with id {} by user id {} and photo id {}", sharedWith, userId, photoId);
        return sharedRepository.existsBySharedWithAndUserId_UserIdAndPhotoId_PhotoId(sharedWith, userId, photoId);
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Shared sharePhoto(Shared sharePhoto) throws SQLException, RuntimeException {
        LOGGER.info("[Shared Translator log] sharePhoto method, input object's date: {}", sharePhoto.getSharedDate());
        return sharedRepository.save(sharePhoto);
    }

    @Override
    public Shared findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId) {
        LOGGER.info("[Shared Translator log] findBySharedIdAndUserId method, shared with id {} and photo id {}", sharedWith, photoId);
        return sharedRepository.findBySharedWithAndPhotoId(sharedWith, photoId);
    }
}
