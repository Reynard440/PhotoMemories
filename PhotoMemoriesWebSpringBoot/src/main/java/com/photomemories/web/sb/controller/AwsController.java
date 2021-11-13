package com.photomemories.web.sb.controller;

import com.photomemories.logic.AwsCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path ="/v1/c4")
@CrossOrigin(origins = "*")
public class AwsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsController.class);
    private final AwsCRUDService awsCRUDService;

    @Autowired
    public AwsController(AwsCRUDService awsCRUDService) {
        this.awsCRUDService = awsCRUDService;
    }

    @PostMapping(
            path = "/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadPhoto(@RequestParam("email") String email,
                            @RequestParam("photos") MultipartFile[] photos) throws IOException {
        LOGGER.info("[AWS Controller log] uploadPhoto method, input email {} ", email);

        for (MultipartFile photo : photos) {
            try {
                LOGGER.info("Photo content-type {}", photo.getContentType());
                awsCRUDService.uploadToS3(email, photo);
            } catch (IOException e) {
                throw new IOException("AWS Controller error: ", e.getCause());
            }
        }
        LOGGER.info("[AWS Controller log] uploadPhoto method, photos uploaded to {}'s folder", email);
    }

    @GetMapping(path = "/displayPhoto/{email}/{imageName}")
    public ResponseEntity<ByteArrayResource> displayPhoto(@PathVariable("email")String email, @PathVariable("imageName")String imageName) {
        LOGGER.info("[AWS Controller log] displayPhoto method, input email {} and imageName {}", email, imageName);
        byte[] imageData = awsCRUDService.downloadPhoto(email, imageName);
        ByteArrayResource arrayResource = new ByteArrayResource(imageData);
        MediaType type;
        String[] newType = imageName.split("\\.");
        switch (newType[1]) {
            case "jpg": { type = new MediaType("image/jpg");}
            case "png": {type = new MediaType("image/tiff");}
            case "bmp": {type = new MediaType("image/bmp");}
            case "jpeg": {type = new MediaType("image/jpeg");}
            case "ico": {type = new MediaType("image/ico");}
            case "gif": {type = new MediaType("image/gif");}
            default: {type = new MediaType("image/png");}
        }
        return ResponseEntity
                .ok()
                .contentLength(imageData.length)
                .contentType(type)
                .body(arrayResource);
    }

    @GetMapping(path = "/downloadPhoto/{email}/{imageName}")
    public ResponseEntity<ByteArrayResource> downloadPhoto(@PathVariable("email")String email, @PathVariable("imageName")String imageName) {
        LOGGER.info("[AWS Controller log] downloadPhoto method, input email {} and imageName {}", email, imageName);
        byte[] imageData = awsCRUDService.downloadPhoto(email, imageName);
        ByteArrayResource arrayResource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .contentLength(imageData.length)
                .contentType(extractContentType(imageName))
                .header("Content-disposition", "attachment; filename=\"" + imageName + "\"")
                .body(arrayResource);
    }


    @DeleteMapping(value = "/deletePhoto")
    public void deletePhoto(
            @RequestParam("fileName") String fileName,
            @RequestParam("email") String email) throws Exception {
        LOGGER.info("[AWS Controller log] deletePhoto method, input fileName {} and email {}", fileName, email);
        awsCRUDService.deletePhoto(fileName, email);
    }

    private MediaType extractContentType (String type) {
        String[] newType = type.split("\\.");
        switch (newType[1]) {
            case "jpg": { return MediaType.valueOf("image/jpg");}
            case "png": {return MediaType.valueOf("image/tiff");}
            case "bmp": {return MediaType.valueOf("image/bmp");}
            case "jpeg": {return MediaType.valueOf("image/jpeg");}
            case "ico": {return MediaType.valueOf("image/ico");}
            case "gif": {return MediaType.valueOf("image/gif");}
            default: {return MediaType.valueOf("image/png");}
        }
    }
}
