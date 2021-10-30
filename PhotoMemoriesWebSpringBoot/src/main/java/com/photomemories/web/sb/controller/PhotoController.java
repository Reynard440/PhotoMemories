package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.PhotoDto;
import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.Shared;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.logic.PhotoCRUDService;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.logic.UserCRUDService;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="/v1/c2")
public class PhotoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    private final PhotoCRUDService photoCRUDService;
    private final AwsCRUDService awsCRUDService;
    private final UserCRUDService userCRUDService;
    private final SharedCRUDService sharedCRUDService;

    @Autowired
    public PhotoController(PhotoCRUDService photoCRUDService, SharedCRUDService sharedCRUDService, AwsCRUDService awsCRUDService, UserCRUDService userCRUDService) {
        this.photoCRUDService = photoCRUDService;
        this.awsCRUDService = awsCRUDService;
        this.userCRUDService = userCRUDService;
        this.sharedCRUDService = sharedCRUDService;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class, Exception.class})
    @PostMapping(
            path = "/addNewPhoto",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new Photo.", notes = "Adds a new Photo in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of the new photo.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<PhotoDto>> addNewPhoto(
            @ApiParam(value = "Date of photo uploaded", example = "2021-10-12", name = "modifiedDate", required = true)
            @RequestParam("modifiedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate modifiedDate,
            @RequestParam("photoLink") String photoLink,
            @RequestParam("photoLocation") String photoLocation,
            @RequestParam("photoSize") double photoSize,
            @ApiParam(value = "Date of photo uploaded", example = "2021-10-28", name = "photoUploadDate", required = true)
            @RequestParam("photoUploadDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate photoUploadDate,
            @RequestParam("photoName") String photoName,
            @RequestParam("photoFormat") String photoFormat,
            @RequestParam("photoCapturedBy") String photoCapturedBy,
            @RequestParam("photoId") Integer photoId,
            @RequestParam("email") String email,
            @RequestParam("userId") Integer userId,
            @RequestParam("photo") MultipartFile photo) throws Exception {
        UserDto user = userCRUDService.getUserDtoByEmail(email);
        if (user != null) {
            SharedDto sharedDto = new SharedDto();
            sharedDto.setSharedHasAccess(false);
            sharedDto.setPhotoId(photoId);
            sharedDto.setSharedDate(LocalDate.now());
            sharedDto.setUserId(userId);
            sharedDto.setSharedWith(0);
            PhotoDto photoDto = new PhotoDto(photoId, photoName, photoSize, LocalDate.now(), LocalDate.now(), photoLink, photoLocation, photoFormat, photoCapturedBy);
            PhotoDto photoResponse = photoCRUDService.createPhotoDto(photoDto);
            SharedDto dto = sharedCRUDService.createSharedDto(sharedDto);
            awsCRUDService.uploadToS3(user.getEmail(), photo);
            PhotoMemoriesResponse<PhotoDto> response = new PhotoMemoriesResponse<>(true, photoResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        throw new RuntimeException("Could not add the photo");
        //return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/photoExists/{id}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> photoExists(
            @ApiParam(value = "The id of each photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws SQLException {
        boolean photoResponse = photoCRUDService.photoExists(id);
        PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/getAllPhotosOfUser/{id}/id/{email}/email")
    @ApiOperation(value = "Gets a list of photos exists based on its id and the users email.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Member found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a member by this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<List<PhotoDto>>> getAllPhotosOfUser(
            @ApiParam(value = "The id of each photo", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id,
            @PathVariable("email") String email) throws SQLException {
        List<PhotoDto> photoResponse = photoCRUDService.getByPhotoIdAndShares_UserId_Email(id, email);
        PhotoMemoriesResponse<List<PhotoDto>> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deletePhoto/{id}")
    @ApiOperation(value = "Deletes a photo by id.", notes = "Removes a photo from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Photo deleted", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve deleting the photo by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the photo with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Integer>> deletePhoto(
            @ApiParam(value = "The id of each user", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws Exception {
        int photoResponse = photoCRUDService.deletePhoto(id);
        PhotoMemoriesResponse<Integer> response = new PhotoMemoriesResponse<>(true, photoResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
