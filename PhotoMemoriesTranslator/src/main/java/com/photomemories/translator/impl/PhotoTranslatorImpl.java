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

    //Dependency Injection: photoRepository is injected and singleton pattern is followed
    @Autowired
    public PhotoTranslatorImpl (PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    //Inserts a photo in the database
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Photo addPhoto(Photo photo) throws SQLException {
        try {
            LOGGER.info("[Photo Translator log] addPhoto method, input object's name: {}", photo.getPhotoName());
            return photoRepository.save(photo);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] addPhoto method, Could not add the new photo record {}", error.getMessage());
            throw new RuntimeException("[Photo Translator Error] addPhoto method, failed to execute the request ", error.getCause());
        }
    }

    //Deletes a photo in the database by given id and photoLink
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer deletePhoto(Integer id, String photoLink) throws Exception {
        try {
            LOGGER.info("[Photo Translator log] deletePhoto method, input id: {}", id);
            int deleteValue = photoRepository.deleteByPhotoIdAndPhotoLink(id, photoLink);
            LOGGER.info("[Photo Translator log] deletePhoto method, (exists?): {}", deleteValue);
            return deleteValue;
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] deletePhoto method, Could not delete the photo {}", error.getMessage());
            throw new RuntimeException("[Photo Translator Error] deletePhoto method, failed to execute the request ", error.getCause());
        }
    }

    //Updates the metadata of a photo in the database with given information
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @Override
    public Integer updatePhoto(String pName, String pLocation, String pCapturedBy, Integer photoId) {
        try {
            return photoRepository.updatePhoto(pName, pLocation, pCapturedBy, photoId);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] updatePhoto method, Could not update the photo {}", error.getMessage());
            throw new RuntimeException("[Photo Translator Error] updatePhoto method, failed to execute the request ", error.getCause());
        }
    }

    //Returns a photo object from the database with given photo id
    @Override
    public Photo getPhotoById(Integer id) {
        try {
            LOGGER.info("[Photo Translator log] getPhotoById method, input id: {}", id);
            return photoRepository.findByPhotoId(id);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] getPhotoById method, Could not get the photo by id {} with error {} ", id, error.getMessage());
            throw new RuntimeException("[Photo Translator Error] getPhotoById method, failed to execute the request ", error.getCause());
        }
    }

    //Filters the photos by name and format in the database
    @Override
    public Photo findByPhotoNameAndPhotoFormat(String name, String format) {
        try {
            LOGGER.info("[Photo Translator log] findByPhotoNameAndPhotoFormat method, input name {} and format {}", name, format);
            return photoRepository.findByPhotoNameAndPhotoFormat(name, format);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] findByPhotoNameAndPhotoFormat method, Could not find the photo with name {} and format {}, with error {}", name, format, error.getMessage());
            throw new RuntimeException("[Photo Translator Error] findByPhotoNameAndPhotoFormat method, failed to execute the request ", error.getCause());
        }
    }

    //Loads all the photos specifically for a shared by id from the database [Shared Entity]
    @Override
    public List<Photo> findByUserEmail(Integer sharedWith) {
        try {
            LOGGER.info("[Photo Translator log] findByUserEmail method, user shared with {}", sharedWith);
            return photoRepository.findByUserPhotosBySharedWith(sharedWith);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] findByUserEmail method, Could not find the user/owner of the photo: id {} not found, with error {} ", sharedWith, error.getMessage());
            throw new RuntimeException("[Photo Translator Error] findByUserEmail method, failed to execute the request ", error.getCause());
        }
    }

    //Checks if a photo exists with specified id and photoLink in the database
    @Override
    public boolean photoExists(Integer id, String photoLink) {
        try {
            LOGGER.info("[Photo Translator log] photoExists method, queried id: {}", id);
            boolean returnValue = photoRepository.existsByPhotoIdAndPhotoLink(id, photoLink);
            LOGGER.info("[Photo Translator log] photoExists method, result: {}", returnValue);
            return returnValue;
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] photoExists method, Could not confirm if the photo exists with id {} and link {}, with error {} ", id, photoLink, error.getMessage());
            throw new RuntimeException("[Photo Translator Error] photoExists method, failed to execute the request ", error.getCause());
        }
    }

    //Loads all the photos from the database
    @Override
    public List<Photo> getAllPhotos() {
        try {
            return photoRepository.findAll();
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] getAllPhotos method, Could not get all the photos, with error {}", error.getMessage());
            throw new RuntimeException("[Photo Translator Error] getAllPhotos method, failed to execute the request ", error.getCause());
        }
    }

    //Loads all the photos specifically for a user by id from the database [Photo Entity]
    @Override
    public List<Photo> getAllPhotosOfUser(Integer userId) {
        try {
            LOGGER.info("[Photo Translator log] getAllPhotosOfUser method, user id {}", userId);
            return photoRepository.findBySharesBySharedWith(userId);
        } catch (RuntimeException error) {
            LOGGER.error("[Photo Translator log] getAllPhotosOfUser method, Could not get all the photos for user id {}, with error {}", userId, error.getMessage());
            throw new RuntimeException("[Photo Translator Error] getAllPhotosOfUser method, failed to execute the request ", error.getCause());
        }
    }
}
