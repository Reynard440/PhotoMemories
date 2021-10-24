package com.photomemories.translator;

import com.photomemories.domain.persistence.Photo;
import org.springframework.stereotype.Component;

@Component
public interface PhotoTranslator {
    Photo addPhoto(Photo photo) throws Exception;

    Photo getPhotoById(Integer id);

    boolean photoExists(Integer id);

    boolean deletePhoto(Integer id) throws Exception;
}
