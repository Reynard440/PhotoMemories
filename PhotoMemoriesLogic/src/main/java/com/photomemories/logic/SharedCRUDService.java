package com.photomemories.logic;

import com.photomemories.domain.dto.SharedDto;

public interface SharedCRUDService {
    SharedDto createSharedDto(SharedDto sharedDto) throws Exception;

    SharedDto getSharedByUserId(Integer id);
}
