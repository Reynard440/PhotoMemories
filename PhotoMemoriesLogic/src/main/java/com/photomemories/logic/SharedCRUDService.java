package com.photomemories.logic;

import com.photomemories.domain.dto.SharedDto;

public interface SharedCRUDService {
    SharedDto createSharedDto(SharedDto sharedDto) throws Exception;

    SharedDto getSHaredByUserId(Integer id);
}
