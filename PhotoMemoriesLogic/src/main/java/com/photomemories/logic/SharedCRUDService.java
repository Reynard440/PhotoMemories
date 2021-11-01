package com.photomemories.logic;

import com.photomemories.domain.dto.SharedDto;

public interface SharedCRUDService {
    SharedDto createSharedDto(SharedDto sharedDto) throws Exception;

    SharedDto getSharedByUserId(Integer id);

    String sharePhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer photoId) throws Exception;
}
