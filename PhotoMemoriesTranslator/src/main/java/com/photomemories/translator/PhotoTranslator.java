package com.photomemories.translator;

import com.photomemories.domain.persistence.Photo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PhotoTranslator {
    Photo addPhoto(Photo photo) throws Exception;

    Photo getPhotoById(Integer id);

    Photo findByPhotoNameAndPhotoFormat(String name, String format);

    List<Photo> findByPhotoIdAndShares_UserId_Email(Integer id, String email);

    boolean photoExists(Integer id, String photoLink);

    Integer deletePhoto(Integer id, String photoLink) throws Exception;

    List<Photo> getAllPhotos();

    List<Photo> getAllPhotosOfUser(Integer userId);
}
