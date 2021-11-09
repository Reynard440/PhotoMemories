package com.photomemories.translator;

import com.photomemories.domain.persistence.Shared;
import org.springframework.stereotype.Component;

@Component
public interface SharedTranslator {
    Shared addShared (Shared shared) throws Exception;

    Shared getSharedBySharedId(Integer id);

    boolean existsBySharedWithAndPhotoId(Integer sharedWith, Integer photoId);

    boolean existsBySharedWithAndUserIdAndPhotoId(Integer sharedWith, Integer userId, Integer photoId);

    Shared sharePhoto (Shared sharePhoto) throws Exception;

    Shared findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId);
}
