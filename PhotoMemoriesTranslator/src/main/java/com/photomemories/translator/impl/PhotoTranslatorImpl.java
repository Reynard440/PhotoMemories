package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.Photo;
import com.photomemories.repo.persistence.PhotoRepository;
import com.photomemories.translator.PhotoTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@Component
public class PhotoTranslatorImpl implements PhotoTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoTranslatorImpl.class);
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoTranslatorImpl (PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Photo addPhoto(Photo photo) throws Exception {
        LOGGER.info("[Photo Translator log] addPhoto method, input object's name: {}", photo.getPhotoName());
        return photoRepository.save(photo);
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer deletePhoto(Integer id, String photoLink) throws Exception {
        LOGGER.info("[Photo Translator log] deletePhoto method, input id: {}", id);
        int deleteValue = photoRepository.deleteByPhotoIdAndPhotoLink(id, photoLink);
        LOGGER.info("[Photo Translator log] deletePhoto method, (exists?): {}", deleteValue);
        return deleteValue;
    }


    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer updatePhoto(String pName, String pLocation, String pCapturedBy, Integer photoId) {
        return photoRepository.updatePhoto(pName, pLocation, pCapturedBy, photoId);
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
    public List<Photo> findByUserEmail(Integer sharedWith) {
        LOGGER.info("[Photo Translator log] findByUserEmail method, user shared with {}", sharedWith);
        return photoRepository.findByUserPhotosBySharedWith(sharedWith);
    }

    @Override
    public boolean photoExists(Integer id, String photoLink) {
        LOGGER.info("[Photo Translator log] photoExists method, queried id: {}", id);
        boolean returnValue = photoRepository.existsByPhotoIdAndPhotoLink(id, photoLink);
        LOGGER.info("[Photo Translator log] photoExists method, result: {}", returnValue);
        return returnValue;
    }

    @Override
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @Override
    public List<Photo> getAllPhotosOfUser(Integer userId) {
        LOGGER.info("[Photo Translator log] getAllPhotosOfUser method, user id {}", userId);
        return photoRepository.findBySharesBySharedWith(userId);
    }
}
