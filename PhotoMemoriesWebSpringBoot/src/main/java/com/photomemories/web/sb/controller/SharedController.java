package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.logic.SharedCRUDService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path="/v1/c3")
public class SharedController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedController.class);
    private final SharedCRUDService sharedCRUDService;
    private final AwsCRUDService awsCRUDService;

    @Autowired
    public SharedController(SharedCRUDService sharedCRUDService, AwsCRUDService awsCRUDService) {
        this.sharedCRUDService = sharedCRUDService;
        this.awsCRUDService = awsCRUDService;
    }

    @PostMapping("/addNewUserRecord")
    @ApiOperation(value = "Create a new Shared record.", notes = "Creates a new Shared record in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Shared record successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of a new shared record.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<SharedDto>> addNewUserRecord(
            @ApiParam(value = "Request body to create a new Shared record", required = true)
            @RequestBody SharedDto sharedDto) throws Exception {
        LOGGER.info("[Shared Controller log] addNewUserRecord method, input object {} ", sharedDto);
        SharedDto sharedResponse = sharedCRUDService.createSharedDto(sharedDto);
        PhotoMemoriesResponse<SharedDto> response = new PhotoMemoriesResponse<>(true, sharedResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sharePhotoWithAnotherUser")
    @ApiOperation(value = "Create a new Shared record.", notes = "Creates a new Shared record in the DB when sharing photos.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Shared record successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of a new shared record.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<String>> sharePhotoWithAnotherUser(
            @ApiParam(value = "Email of the sharing user", required = true, example = "reynardengels@gmail.com")
            @RequestParam("sharingEmail") String sharingEmail,
            @ApiParam(value = "Email of the receiving user", required = true, example = "rudidreyer6@gmail.com")
            @RequestParam("receivingEmail") String receivingEmail,
            @ApiParam(value = "Access rights to delete the photo", required = true, example = "true")
            @RequestParam("accessRights") boolean accessRights,
            @ApiParam(value = "Id of the sharing photo", required = true, example = "2")
            @RequestParam("id") Integer id,
            @ApiParam(value = "The photo the user wants to share", required = true)
            @RequestParam("photos") MultipartFile[] photos) throws Exception {
        LOGGER.info("[Shared Controller log] sharePhotoWithAnotherUser method, input sharingEmail {} receivingEmail {} accessRights {} id {} ", sharingEmail, receivingEmail, accessRights, id);
        String sharedResponse = sharedCRUDService.sharePhoto(sharingEmail, receivingEmail, accessRights, id);
        for (MultipartFile photo : photos) {
            try {
                awsCRUDService.uploadToS3(receivingEmail, photo);
            } catch (IOException e) {
                throw new IOException("An error occurred: ", e.getCause());
            }
        }
        LOGGER.info("[AWS Controller log] uploadPhoto method, photos uploaded to {}'s folder", receivingEmail);
        PhotoMemoriesResponse<String> response = new PhotoMemoriesResponse<>(true, sharedResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
