package com.photomemories.logic;

import com.photomemories.domain.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoCRUDService {
    PhotoDto createPhotoDto(PhotoDto photoDto, String email, MultipartFile photoFile) throws Exception;

    PhotoDto getPhotoDtoById(Integer id);

    PhotoDto getByPhotoNameAndPhotoFormat(String name, String format);

    List<PhotoDto> getByPhotoIdAndShares_UserId_Email(Integer id, String email);

    List<PhotoDto> getAllPhotos();

    boolean photoExists(Integer id, String photoLink);

    Integer deletePhoto(Integer id, String photoLink, String email) throws Exception;
}
