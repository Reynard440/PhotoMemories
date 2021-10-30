package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
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
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping(path="/v1/c1")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    private final UserCRUDService userCRUDService;

    @Autowired
    public UserController(UserCRUDService userCRUDService) {
        this.userCRUDService = userCRUDService;
    }

    @PostMapping("/addNewUser")
    @ApiOperation(value = "Create a new User.", notes = "Creates a new User in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of a new user.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> addNewUser(
            @ApiParam(value = "Request body to create a new User", required = true)
            @RequestBody UserDto userDto) throws Exception {
        LOGGER.info("[User Controller log] addNewUser method, input object {} ", userDto);
        UserDto userResponse = userCRUDService.createNewUser(userDto);
        PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/userExists/{id}")
    @ApiOperation(value = "Checks if a user exists based on their id.", notes = "Tries to fetch a user by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User exists", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the user with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> userExists(
            @ApiParam(value = "The id of each user", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws SQLException {
        LOGGER.info("[User Controller log] userExists method, input id {} ", id);
        boolean userResponse = userCRUDService.userExists(id);
        PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "Checks if a user's login credentials are valid.", notes = "Checks if a user's login credentials are valid in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User logged in", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve user login", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> login(
            @ApiParam(value = "The password of the user", example = "$2a$10$8SPELKOF4/Mva/QYtLGAZuKMyBKeeN7JkObqhYrLQ5Bwb1lB8Bi52", name = "password", required = true)
            @RequestParam("password") String password,
            @ApiParam(value = "The email of the user", example = "reynardnegels@gmail.com", name = "email", required = true)
            @RequestParam("email") String email) throws Exception {
        LOGGER.info("[User Controller log] login method, input username {} and password {} ", email, password);
        boolean userResponse = userCRUDService.loginUser(password, email);
        PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{id}")
    @ApiOperation(value = "Deletes a user by id.", notes = "Removes a user from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve deleting the user by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the user with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Integer>> deleteUser(
            @ApiParam(value = "The id of each user", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws Exception {
        LOGGER.info("[User Controller log] deleteUser method, input id {} ", id);
        int userResponse = userCRUDService.deleteUser(id);
        PhotoMemoriesResponse<Integer> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //TODO: Update photo method

    @GetMapping("/getUserById/{id}")
    @ApiOperation(value = "Checks if a photo exists based on their id.", notes = "Tries to fetch a photo by id from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a user with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> getUserById(
            @ApiParam(value = "The id of each user", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id) throws SQLException {
        LOGGER.info("[User Controller log] getUserById method, input id {} ", id);
        UserDto userResponse = userCRUDService.getUserDtoById(id);
        PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
