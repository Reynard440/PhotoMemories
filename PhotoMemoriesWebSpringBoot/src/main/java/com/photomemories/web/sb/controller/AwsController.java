package com.photomemories.web.sb.controller;

import com.photomemories.logic.AwsCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public void uploadPhoto(@RequestParam("userId") Integer userId,
                                       @RequestParam("photo") MultipartFile photo) {
        awsCRUDService.uploadToS3(userId, photo);
    }

    @GetMapping(
            path = "{userProfileId}/image/{imageName}/download",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] downloadPhoto(@PathVariable("userProfileId")Integer userProfileId, @PathVariable("imageName")String imageName) {
        return awsCRUDService.downloadPhoto(userProfileId, imageName);
    }

    @GetMapping("{id}/user/photos")
    public List<Object> getAllPhotos(@PathVariable("id")Integer id) {
        return awsCRUDService.getAllPhotosOfUser(id);
    }
}
