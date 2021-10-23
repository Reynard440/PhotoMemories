package com.photomemories.translator;

import com.photomemories.domain.persistence.Photo;
import org.springframework.stereotype.Component;

@Component
public interface PhotoTranslator {
    Photo addPhoto(Photo photo) throws Exception;

    boolean photoExists(Integer id);
}
