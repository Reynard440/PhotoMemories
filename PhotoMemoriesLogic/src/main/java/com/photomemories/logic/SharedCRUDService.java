package com.photomemories.logic;

import com.photomemories.domain.dto.SharedDto;
import org.springframework.web.multipart.MultipartFile;

public interface SharedCRUDService {
    SharedDto createSharedDto(SharedDto sharedDto) throws Exception;

    SharedDto getSharedByUserId(Integer id);

    String sharePhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer photoId, MultipartFile photo) throws Exception;

//    SharedDto findBySharedDtoVerifiedIds(Integer sharedWitdId, Integer userId, Integer photoId);
}
