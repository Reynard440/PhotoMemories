package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.PhotoDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.PhotoCRUDService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="/v1/c2")
@CrossOrigin(origins = "http://localhost:3000")
public class PhotoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    private final PhotoCRUDService photoCRUDService;

    @Autowired
    public PhotoController(PhotoCRUDService photoCRUDService) {
        this.photoCRUDService = photoCRUDService;
    }

    @PostMapping(
            path = "/addNewPhoto",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new Photo.", notes = "Adds a new Photo in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of the new photo.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<PhotoDto>> addNewPhoto(
            @ApiParam(value = "Date of photo uploaded", example = "2021-10-12", name = "modifiedDate")
            @RequestParam("modifiedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate modifiedDate,
            @RequestParam("photoName") String photoName,
            @RequestParam("photoLocation") String photoLocation,
            @RequestParam("photoCapturedBy") String photoCapturedBy,
            @RequestParam("email") String email,
            @RequestParam("photo") MultipartFile photo) throws Exception {
            try {
                PhotoDto photoDto = new PhotoDto(photoName, (double)photo.getSize(), LocalDate.now(), modifiedDate, photo.getOriginalFilename(), photoLocation, photo.getContentType(), photoCapturedBy);
                PhotoDto photoResponse = photoCRUDService.createPhotoDto(photoDto, email, photo);
                LOGGER.info("[Photo Controller log] addNewPhoto method, photos uploaded to {}'s folder", email);

                PhotoMemoriesResponse<PhotoDto> response = new PhotoMemoriesResponse<>(true, photoResponse);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (IOException e) {
                throw new IOException("[Photo Controller Error] addNewPhoto method, An error occurred: ", e.getCause());
            }
    }

    @GetMapping("/photoExists/{id}/{photoLink}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> photoExists(
            @ApiParam(value = "The id of each photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "The name of each photo", example = "ReynardEngels.jpeg", name = "photoLink", required = true)
            @PathVariable("photoLink") String photoLink) throws SQLException {
        LOGGER.info("[Photo Controller log] photoExists method, input id {} and photoLink {}", id, photoLink);
        boolean photoResponse = photoCRUDService.photoExists(id, photoLink);
        PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllPhotosOfUser/{id}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<List<PhotoDto>>> getAllPhotosOfUser(
            @ApiParam(value = "The id of each photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws SQLException {
        LOGGER.info("[Photo Controller log] getAllPhotosOfUser method, input user id {}", id);
        List<PhotoDto> photoResponse = photoCRUDService.getAllPhotoDtosOfUser(id);
        PhotoMemoriesResponse<List<PhotoDto>> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getAllPhotosForUser/{email}")
    @ApiOperation(value = "Checks if a user exists based on their email.", notes = "Tries to fetch a user by email from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a user by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public List<byte[]> viewAllUserPhotos(@PathVariable("email")String email) {
        LOGGER.info("[Photo Controller log] getAllPhotosForUser method, input email {}", email);
        List<byte[]> listOfPhotos = photoCRUDService.getAllPhotosForUser(email);
        LOGGER.info("[Photo Controller log] getAllPhotosForUser method, {} photos were retrieved", listOfPhotos.size());
        return listOfPhotos;
    }

    @GetMapping("/getPhotoById/{id}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<PhotoDto>> getPhotoById(
            @ApiParam(value = "The id of each photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws SQLException {
        LOGGER.info("[Photo Controller log] getPhotoById method, input id {} ", id);
        PhotoDto photoResponse = photoCRUDService.getPhotoDtoById(id);
        PhotoMemoriesResponse<PhotoDto> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllPhotos")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<List<PhotoDto>>> getAllPhotos() throws SQLException {
        List<PhotoDto> photoResponse = photoCRUDService.getAllPhotos();
        PhotoMemoriesResponse<List<PhotoDto>> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/loadAllPhotosOfUser/{email}/")
    @ApiOperation(value = "Gets a list of photos exists based on its id and the users email.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo found by user email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by user's email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the user by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<List<PhotoDto>>> loadAllPhotosOfUser(
            @PathVariable("email") String email) throws SQLException {
        LOGGER.info("[Photo Controller log] getAllPhotosOfUser method, email {} ", email);
        List<PhotoDto> photoResponse = photoCRUDService.getPhotosByUserEmail(email);
        PhotoMemoriesResponse<List<PhotoDto>> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(rollbackOn = {RuntimeException.class, Exception.class})
    @DeleteMapping("/deletePhoto/{photoLink}/{email}/{id}")
    @ApiOperation(value = "Deletes a photo by id.", notes = "Removes a photo from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo deleted", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve deleting the photo by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the photo with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Integer>> deletePhoto(
            @ApiParam(value = "The name of the photo", example = "ReynardEngels.jpeg", name = "photoLink", required = true)
            @PathVariable("photoLink") String photoLink,
            @ApiParam(value = "The email of the user", example = "reynardengels@gmail.com", name = "email", required = true)
            @PathVariable(value = "email") String email,
            @ApiParam(value = "The id of the photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws Exception {
        LOGGER.info("[Photo Controller log] deletePhoto method, input id {} and email {} and photoLink {}", id, email, photoLink);
        int photoResponse = photoCRUDService.deletePhotoDto(id, photoLink, email);
        PhotoMemoriesResponse<Integer> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @PutMapping("/updateMetadata/{id}")
    @ApiOperation(value = "Updates a photo's metadata by id.", notes = "Changes a photo's metadata in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo updated", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve updating the photo by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the photo with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<PhotoDto>> updateMetadata(
            @ApiParam(value = "The id of the photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "The photo's new  name.", example = "ReynardEngels.jpeg", name = "pName", required = true)
            @RequestParam("pName") String pName,
            @ApiParam(value = "The photo's new location.", example = "Vaalpark", name = "pLocation", required = true)
            @RequestParam("pLocation") String pLocation,
            @ApiParam(value = "The photo's new owner.", example = "Reyno Engels", name = "pCaptured", required = true)
            @RequestParam("pCaptured") String pCaptured,
            @ApiParam(value = "The email of the user trying to update the photo.", example = "reynardengels@gmail.com", name = "email", required = true)
            @RequestParam("email") String email) throws Exception {
        LOGGER.info("[Photo Controller log] updateMetadata method, input id {} ", id);

        if (photoCRUDService.photoExists(id, pName)) {
            LOGGER.error("[Photo Controller log] updateMetadata method, photo {} does not exist", pName);
            throw new RuntimeException("[Photo Controller Error] updateMetadata method, photo " + pName + " does not exist");
        }

        PhotoDto photoResponse = photoCRUDService.updatePhotoDto(pName, pLocation, pCaptured, id, email);
        PhotoMemoriesResponse<PhotoDto> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
