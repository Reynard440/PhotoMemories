package com.photomemories.logic;

import com.photomemories.domain.dto.PhotoDto;

public interface PhotoCRUDService {
    PhotoDto createPhotoDto(PhotoDto photoDto) throws Exception;

    boolean photoExists(Integer id);
}
