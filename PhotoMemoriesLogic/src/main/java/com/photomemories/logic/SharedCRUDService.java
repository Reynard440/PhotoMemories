package com.photomemories.logic;

import com.photomemories.domain.dto.SharedDto;
import org.springframework.web.multipart.MultipartFile;

public interface SharedCRUDService {
    SharedDto createSharedDto(SharedDto sharedDto) throws Exception;

    SharedDto getSharedBySharedId(Integer id);

    SharedDto findBySharedWithAndPhotoId(Integer sharedWith, Integer photoId);

    String sharePhoto(String sharingEmail, String receivingEmail, boolean accessRights, Integer photoId, MultipartFile photo) throws Exception;

    boolean checkBySharedWithAndPhotoId(String email, Integer photoId) throws Exception;

    boolean existsBySharedWithAndUserIdAndPhotoId(String email, Integer photoId) throws Exception;
}
