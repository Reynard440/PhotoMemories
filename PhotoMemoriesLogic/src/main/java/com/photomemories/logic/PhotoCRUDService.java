package com.photomemories.logic;

import com.photomemories.domain.dto.PhotoDto;

public interface PhotoCRUDService {
    PhotoDto createPhotoDto(PhotoDto photoDto) throws Exception;

    PhotoDto getPhotoDtoById(Integer id);

    boolean photoExists(Integer id);

    boolean deletePhoto(Integer id) throws Exception;
}
