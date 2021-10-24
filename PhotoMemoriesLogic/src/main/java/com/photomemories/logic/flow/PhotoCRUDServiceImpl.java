package com.photomemories.logic.flow;

import com.photomemories.domain.dto.PhotoDto;
import com.photomemories.domain.persistence.Photo;
import com.photomemories.logic.PhotoCRUDService;
import com.photomemories.translator.PhotoTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("photoServiceFlow")
public class PhotoCRUDServiceImpl implements PhotoCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoCRUDServiceImpl.class);

    private final PhotoTranslator photoTranslator;

    @Autowired
    public PhotoCRUDServiceImpl(PhotoTranslator photoTranslator) {
        this.photoTranslator = photoTranslator;
    }

    @Override
    public PhotoDto createPhotoDto(PhotoDto photoDto) throws Exception {
        try {
            LOGGER.info("[Photo Logic log] input Dto object is: {}", photoDto);
            Photo photo = photoDto.buildPhoto();
            LOGGER.info("[Photo Logic log] Dto Object converted to persistence is: {}", photo);
            photo.setUploadDate(LocalDate.now());
            Photo addedPhoto = photoTranslator.addPhoto(photo);
            LOGGER.info("[Photo Logic log] object saved to database: {}", addedPhoto);
            PhotoDto returnDto = new PhotoDto(photoTranslator.addPhoto(addedPhoto));
            LOGGER.info("[Photo Logic log] Dto returned: {}", returnDto);
            return returnDto;
        } catch (Exception e) {
            throw new RuntimeException("Photo could not be created!", e);
        }
    }

    @Override
    public PhotoDto getPhotoDtoById(Integer id) {
        return new PhotoDto(photoTranslator.getPhotoById(id));
    }

    @Override
    public boolean photoExists(Integer id) {
        LOGGER.info("[Photo Logic log] photoExists method, queried id: {}", id);
        boolean returnPhotoLogicValue = photoTranslator.photoExists(id);
        LOGGER.info("[Photo Logic log] photoExists method, result: {}", returnPhotoLogicValue);
        return returnPhotoLogicValue;
    }

    @Override
    public boolean deletePhoto(Integer id) throws Exception {
        LOGGER.info("[Photo Logic log] deletePhoto method, queried id: {}", id);
        boolean beforeDelete = photoTranslator.photoExists(id);
        LOGGER.info("[Photo Logic log] deletePhoto method, (exists?): {}", beforeDelete);
        if (!photoExists(id)) {
            LOGGER.warn("Photo with id {} does not exists", id);
            throw new RuntimeException("Photo deletion error");
        }
        boolean photoDelete = photoTranslator.deletePhoto(id);
        boolean afterDelete = photoTranslator.photoExists(id);
        LOGGER.info("[Photo Logic log] deletePhoto method, (exists?): {}", afterDelete);
        return photoDelete;
    }
}
