package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.UserCRUDService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(path="/v1/c1")
public class UserController {
    private final UserCRUDService userCRUDService;

    @Autowired
    public UserController(UserCRUDService userCRUDService) {
        this.userCRUDService = userCRUDService;
    }

    @PostMapping("/addUser")
    @ApiOperation(value = "Create a new User.", notes = "Creates a new User in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of a new user.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> newUser(
            @ApiParam(value = "Request body to create a new User", required = true)
            @RequestBody UserDto userDto) throws Exception {
        UserDto userResponse = userCRUDService.createNewUser(userDto);
        PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
