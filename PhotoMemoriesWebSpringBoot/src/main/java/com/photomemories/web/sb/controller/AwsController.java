package com.photomemories.web.sb.controller;

import com.amazonaws.services.s3.model.ObjectListing;
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
import java.util.Arrays;

@RestController
@RequestMapping(path ="/v1/c4")
@CrossOrigin("*")
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

        Arrays.asList(photos).stream().forEach(photo -> {
            try {
                awsCRUDService.uploadToS3(email, photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        LOGGER.info("[AWS Controller log] uploadPhoto method, photos uploaded to {}'s folder", email);
    }

    @GetMapping(path = "/{email}/image/{imageName}/download")
    public ResponseEntity<ByteArrayResource> downloadPhoto(@PathVariable("email")String email, @PathVariable("imageName")String imageName) {
        LOGGER.info("[AWS Controller log] downloadPhoto method, input email {} and imageName {}", email, imageName);
        byte[] imageData = awsCRUDService.downloadPhoto(email, imageName);
        ByteArrayResource arrayResource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .contentLength(imageData.length)
                .header("Content-type", "application/octet-stream")
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

    //TODO: Update photo method

    @GetMapping("{folderName}/user/photos")
    public ObjectListing getAllPhotos(@PathVariable("folderName")String folderName) {
        LOGGER.info("[AWS Controller log] getAllPhotos method, Photos retrieved from {}", folderName);
        return awsCRUDService.getAllPhotosOfUser(folderName);
    }
}
