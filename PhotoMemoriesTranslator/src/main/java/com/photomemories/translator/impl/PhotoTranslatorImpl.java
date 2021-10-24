package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.Photo;
import com.photomemories.repo.persistence.PhotoRepository;
import com.photomemories.translator.PhotoTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoTranslatorImpl implements PhotoTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoTranslatorImpl.class);
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoTranslatorImpl (PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo addPhoto(Photo photo) throws Exception {
        LOGGER.info("[Photo Translator log] addPhoto method, input object: {}", photo);
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPhotoById(Integer id) {
        return photoRepository.findByPhotoId(id);
    }

    @Override
    public boolean photoExists(Integer id) {
        LOGGER.info("[Photo Translator log] photoExists method, queried id: {}", id);
        boolean returnValue = photoRepository.existsByPhotoId(id);
        LOGGER.info("[Photo Translator log] photoExists method, result: {}", returnValue);
        return returnValue;
    }

    @Override
    public boolean deletePhoto(Integer id) throws Exception {
        LOGGER.info("[Photo Translator log] deletePhoto method, input id: {}", id);
        boolean deleteValue = photoRepository.deleteByPhotoId(id);
        LOGGER.info("[Photo Translator log] deletePhoto method, (exists?): {}", deleteValue);
        return deleteValue;
    }

    //TODO: Update method
    //TODO: Get by metadata method
}
