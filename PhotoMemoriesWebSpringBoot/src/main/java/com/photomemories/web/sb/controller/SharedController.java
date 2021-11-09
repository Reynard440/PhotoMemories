package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
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

import java.sql.SQLException;

@RestController
@RequestMapping(path="/v1/c3")
@CrossOrigin(origins = "http://localhost:3000")
public class SharedController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedController.class);
    private final SharedCRUDService sharedCRUDService;

    @Autowired
    public SharedController(SharedCRUDService sharedCRUDService) {
        this.sharedCRUDService = sharedCRUDService;
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
            @RequestParam("photo") MultipartFile photo) throws Exception {
        LOGGER.info("[Shared Controller log] sharePhotoWithAnotherUser method, input sharingEmail {} receivingEmail {} accessRights {} id {} ", sharingEmail, receivingEmail, accessRights, id);

        if (sharedCRUDService.existsBySharedWithAndUserIdAndPhotoId(receivingEmail, id)) {
            LOGGER.error("[Shared Controller log] sharePhotoWithAnotherUser method, user {} already has this photo", receivingEmail);
            throw new SQLException("[Shared Controller Error] sharePhotoWithAnotherUser method, user already has this photo");
        }

        String sharedResponse = sharedCRUDService.sharePhoto(sharingEmail, receivingEmail, accessRights, id, photo);
        LOGGER.info("[AWS Controller log] uploadPhoto method, photos uploaded to {}'s folder", receivingEmail);
        PhotoMemoriesResponse<String> response = new PhotoMemoriesResponse<>(true, sharedResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getSharedBySharedWithAndEmail/{email}/{id}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Shared record found by id and user email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by id and email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a shared record with this id and user email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> getSharedBySharedWithAndEmail(
            @ApiParam(value = "The email of the user", example = "1", name = "email", required = true)
            @PathVariable("email") String email,
            @ApiParam(value = "The id of the photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws Exception {
        LOGGER.info("[Shared Controller log] getSharedByIdAndEmail method, input photo id {} and user email {}", id, email);
        boolean isFound = sharedCRUDService.checkBySharedWithAndPhotoId(email, id);
        PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, isFound);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
