package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.SharedDto;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.SharedCRUDService;
import com.photomemories.logic.UserCRUDService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/v1/c3")
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
        SharedDto sharedResponse = sharedCRUDService.createSharedDto(sharedDto);
        PhotoMemoriesResponse<SharedDto> response = new PhotoMemoriesResponse<>(true, sharedResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
