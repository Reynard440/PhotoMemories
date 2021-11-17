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

    //Dependency Injection: awsCRUDService is injected to ensure singleton pattern is followed
    @Autowired
    public AwsController(AwsCRUDService awsCRUDService) {
        this.awsCRUDService = awsCRUDService;
    }

    //Saving to the S3 bucket on AWS
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
            } catch (RuntimeException e) {
                throw new IOException("AWS Controller error: ", e.getCause());
            }
        }
        LOGGER.info("[AWS Controller log] uploadPhoto method, photos uploaded to {}'s folder", email);
    }

    //Displays a photo from AWS
    @GetMapping(path = "/displayPhoto/{email}/{imageName}")
    public ResponseEntity<ByteArrayResource> displayPhoto(@PathVariable("email")String email, @PathVariable("imageName")String imageName) throws Exception {
        try {
            LOGGER.info("[AWS Controller log] displayPhoto method, input email {} and imageName {}", email, imageName);
            byte[] imageData = awsCRUDService.downloadPhoto(email, imageName);
            ByteArrayResource arrayResource = new ByteArrayResource(imageData);
            return ResponseEntity
                    .ok()
                    .contentLength(imageData.length)
                    .contentType(extractContentType(imageName))
                    .body(arrayResource);
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Controller log] displayPhoto method, Could not display photo from AWS, with error {} ", error.getMessage());
            throw new RuntimeException("[AWS Controller Error] displayPhoto method, failed to execute the request", error.getCause());
        }
    }

    //Downloads a photo from AWS
    @GetMapping(path = "/downloadPhoto/{email}/{imageName}")
    public ResponseEntity<ByteArrayResource> downloadPhoto(@PathVariable("email")String email, @PathVariable("imageName")String imageName) throws Exception {
        try {
            LOGGER.info("[AWS Controller log] downloadPhoto method, input email {} and imageName {}", email, imageName);
            byte[] imageData = awsCRUDService.downloadPhoto(email, imageName);
            ByteArrayResource arrayResource = new ByteArrayResource(imageData);
            return ResponseEntity
                    .ok()
                    .contentLength(imageData.length)
                    .contentType(extractContentType(imageName))
                    .header("Content-disposition", "attachment; filename=\"" + imageName + "\"")
                    .body(arrayResource);
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Controller log] downloadPhoto method, Could not download photo from AWS, with error {} ", error.getMessage());
            throw new RuntimeException("[AWS Controller Error] downloadPhoto method, failed to execute the request", error.getCause());
        }
    }

    //Deletes a photo from AWS
    @DeleteMapping(value = "/deletePhoto")
    public void deletePhoto(
            @RequestParam("fileName") String fileName,
            @RequestParam("email") String email) throws Exception {
        try {
            LOGGER.info("[AWS Controller log] deletePhoto method, input fileName {} and email {}", fileName, email);
            awsCRUDService.deletePhoto(fileName, email);
        } catch (RuntimeException error) {
            LOGGER.error("[AWS Controller log] deletePhoto method, Could not delete photo from AWS, with error {} ", error.getMessage());
            throw new RuntimeException("[AWS Controller Error] deletePhoto method, failed to execute the request", error.getCause());
        }
    }

    //Private method used to extract the image format for both the display and download methods
    private MediaType extractContentType (String type) {
        String[] newType = type.split("\\.");
        switch (newType[1]) {
            case "jpg": { return MediaType.valueOf("image/jpg");}
            case "png": {return MediaType.valueOf("image/png");}
            case "bmp": {return MediaType.valueOf("image/bmp");}
            case "jpeg": {return MediaType.valueOf("image/jpeg");}
            case "ico": {return MediaType.valueOf("image/ico");}
            case "gif": {return MediaType.valueOf("image/gif");}
            default: {return MediaType.valueOf("image/tiff");}
        }
    }
}
