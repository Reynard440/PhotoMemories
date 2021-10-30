package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.Photo;
import com.photomemories.repo.persistence.PhotoRepository;
import com.photomemories.translator.PhotoTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

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
        LOGGER.info("[Photo Translator log] addPhoto method, input object's name: {}", photo.getPhotoName());
        return photoRepository.save(photo);
    }

    @Override
    public Photo getPhotoById(Integer id) {
        LOGGER.info("[Photo Translator log] getPhotoById method, input id: {}", id);
        return photoRepository.findByPhotoId(id);
    }

    @Override
    public Photo findByPhotoNameAndPhotoFormat(String name, String format) {
        LOGGER.info("[Photo Translator log] findByPhotoNameAndPhotoFormat method, input name {} and format {}", name, format);
        return photoRepository.findByPhotoNameAndPhotoFormat(name, format);
    }

    @Override
    public List<Photo> findByPhotoIdAndShares_UserId_Email(Integer id, String email) {
        LOGGER.info("[Photo Translator log] findByPhotoIdAndShares_UserId_Email method, input id {} and user email {}", id, email);
        return photoRepository.findByPhotoIdAndShares_UserId_Email(id, email);
    }

    @Override
    public boolean photoExists(Integer id, String photoLink) {
        LOGGER.info("[Photo Translator log] photoExists method, queried id: {}", id);
        boolean returnValue = photoRepository.existsByPhotoIdAndPhotoLink(id, photoLink);
        LOGGER.info("[Photo Translator log] photoExists method, result: {}", returnValue);
        return returnValue;
    }

    @Transactional(rollbackOn = {RuntimeException.class, Exception.class})
    @Override
    public Integer deletePhoto(Integer id, String photoLink) throws Exception {
        LOGGER.info("[Photo Translator log] deletePhoto method, input id: {}", id);
        int deleteValue = photoRepository.deleteByPhotoIdAndPhotoLink(id, photoLink);
        LOGGER.info("[Photo Translator log] deletePhoto method, (exists?): {}", deleteValue);
        return deleteValue;
    }

    @Override
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    //TODO: Update method
    //TODO: Get by metadata method
}
