package com.photomemories.logic;

import com.photomemories.domain.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

public interface PhotoCRUDService {
    PhotoDto createPhotoDto(PhotoDto photoDto, String email, MultipartFile photoFile) throws Exception;

    PhotoDto getPhotoDtoById(Integer id);

    String sendPhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer photoId);

    List<PhotoDto> getPhotosByUserEmail(String email);

    List<PhotoDto> getAllPhotos();

    boolean photoExists(Integer id, String photoLink);

    Integer deletePhotoDto(Integer id, String photoLink, String email) throws Exception;

    PhotoDto updatePhotoDto(String pName, String pLocation, String pCapturedBy, Integer photoId, String email) throws SQLException;
}
