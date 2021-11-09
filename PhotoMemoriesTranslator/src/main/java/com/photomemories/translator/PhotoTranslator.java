package com.photomemories.translator;

import com.photomemories.domain.persistence.Photo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PhotoTranslator {
    Photo addPhoto(Photo photo) throws Exception;

    Photo getPhotoById(Integer id);

    Photo findByPhotoNameAndPhotoFormat(String name, String format);

    List<Photo> findByUserEmail(Integer sharedWith);

    boolean photoExists(Integer id, String photoLink);

    Integer deletePhoto(Integer id, String photoLink) throws Exception;

    List<Photo> getAllPhotos();

    List<Photo> getAllPhotosOfUser(Integer userId);

    Integer updatePhoto(String pName, String pLocation, String pCapturedBy, Integer photoId);
}
