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

    //Dependency Injection: sharedRepository is injected and singleton pattern is followed
    @Autowired
    public SharedTranslatorImpl (SharedRepository sharedRepository) {
        this.sharedRepository = sharedRepository;
    }

    //Inserts a shared record in the database
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Shared addShared(Shared shared) throws RuntimeException, SQLException {
        try {
            LOGGER.info("[Shared Translator log] addShared method, input object's date {}", shared.getSharedDate());
            return sharedRepository.save(shared);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] addShared method, Could not add the new shared record {}", error.getMessage());
            throw new RuntimeException("[Shared Translator Error] addShared method, failed to execute the request ", error.getCause());
        }
    }

    //Deletes a shared record from the database by given sharedWith id and photo id
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Integer deleteBySharedRecord(Integer sharedWith, Integer photoId) throws SQLException {
        try {
            LOGGER.info("[Shared Translator log] deleteBySharedRecord method, delete photo {} for user {} ", sharedWith, photoId);
            return sharedRepository.deleteBySharedWithAndPhotoId_PhotoId(sharedWith, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] deleteBySharedRecord method, Could not delete the shared record with sharedWith id {} and photo id {}, with error {}", sharedWith, photoId, error.getMessage());
            throw new RuntimeException("[Shared Translator Error] deleteBySharedRecord method, failed to execute the request ", error.getCause());
        }
    }

    //Checks if a shared record exists given te set of parameters
    @Override
    public boolean existsBySharedWithAndPhotoId(Integer sharedWith, Integer photoId) {
        try {
            LOGGER.info("[Shared Translator log] existsBySharedWithAndPhotoId method, shared with id {} and photoId {} ", sharedWith, photoId);
            return sharedRepository.existsBySharedWithAndPhotoId_PhotoId(sharedWith, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] existsBySharedWithAndPhotoId method, Could not find the shared record with shareWith id {} and photo id {}, with error {}", sharedWith, photoId, error.getMessage());
            throw new RuntimeException("[Shared Translator Error] existsBySharedWithAndPhotoId method, failed to execute the request ", error.getCause());
        }
    }

    //Checks to see if a shred record exists with the given parameters
    @Override
    public boolean existsBySharedWithAndUserIdAndPhotoId(Integer sharedWith, Integer userId, Integer photoId) {
        try {
            LOGGER.info("[Shared Translator log] existsBySharedWithAndUserIdAndPhotoId method, shared with id {} by user id {} and photo id {}", sharedWith, userId, photoId);
            return sharedRepository.existsBySharedWithAndUserId_UserIdAndPhotoId_PhotoId(sharedWith, userId, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] existsBySharedWithAndUserIdAndPhotoId method, Shared record with sharedWith id {}, user id {}, and photo id {} does not exist, with error {}", sharedWith, userId, photoId, error.getMessage());
            throw new RuntimeException("[Shared Translator Error] existsBySharedWithAndUserIdAndPhotoId method, failed to execute the request ", error.getCause());
        }
    }

    //Inserts a photo in the database
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Shared sharePhoto(Shared sharePhoto) throws SQLException, RuntimeException {
        try {
            LOGGER.info("[Shared Translator log] sharePhoto method, input object's date: {}", sharePhoto.getSharedDate());
            return sharedRepository.save(sharePhoto);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] sharePhoto method, Could not share the share with error {}", error.getMessage());
            throw new RuntimeException("[Shared Translator Error] sharePhoto method, failed to execute the request ", error.getCause());
        }
    }

    //Returns a shared object from the database with given sharedWith and photo id
    @Override
    public Shared findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId) {
        try {
            LOGGER.info("[Shared Translator log] findBySharedIdAndUserId method, shared with id {} and photo id {}", sharedWith, photoId);
            return sharedRepository.findBySharedWithAndPhotoId(sharedWith, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Shared Translator log] findBySharedWithAndPhotoId method, Could not find the shared record by id {} and photo id {}, with error {}", sharedWith, photoId, error.getMessage());
            throw new RuntimeException("[Shared Translator Error] findBySharedWithAndPhotoId method, failed to execute the request ", error.getCause());
        }
    }
}
