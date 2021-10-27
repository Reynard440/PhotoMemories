package com.photomemories.logic;

import com.photomemories.domain.dto.PhotoDto;

import java.util.List;

public interface PhotoCRUDService {
    PhotoDto createPhotoDto(PhotoDto photoDto) throws Exception;

    PhotoDto getPhotoDtoById(Integer id);

    PhotoDto getByPhotoNameAndPhotoFormat(String name, String format);

    List<PhotoDto> getByPhotoIdAndShares_UserId_Email(Integer id, String email);

    List<PhotoDto> getAllPhotos();

    boolean photoExists(Integer id);

    Integer deletePhoto(Integer id) throws Exception;
}
