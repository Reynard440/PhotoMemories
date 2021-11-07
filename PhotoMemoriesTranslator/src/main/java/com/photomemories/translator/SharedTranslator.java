package com.photomemories.translator;

import com.photomemories.domain.persistence.Shared;
import org.springframework.stereotype.Component;

@Component
public interface SharedTranslator {
    Shared addShared (Shared shared) throws Exception;

    Shared getSharedByUserId(Integer id);

    Shared sharePhoto (Shared sharePhoto) throws Exception;

//    Shared findBySharedVerifiedIds(Integer sharedWitdId, Integer UserId, Integer PhotoId);
}
