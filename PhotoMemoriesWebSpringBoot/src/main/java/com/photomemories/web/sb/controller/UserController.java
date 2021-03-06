package com.photomemories.web.sb.controller;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.service.PhotoMemoriesResponse;
import com.photomemories.logic.AwsCRUDService;
import com.photomemories.logic.UserCRUDService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;

@RestController
@RequestMapping(path="/v1/c1")
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
    private final UserCRUDService userCRUDService;
    private final AwsCRUDService awsCRUDService;

    //Dependency Injection: userCRUDService and awsCRUDService are injected and singleton pattern is followed
    @Autowired
    public UserController(UserCRUDService userCRUDService, AwsCRUDService awsCRUDService) {
        this.userCRUDService = userCRUDService;
        this.awsCRUDService = awsCRUDService;
    }

    //Inserts a user to the database, and adds it to spring security config for login purposes
    @Transactional(rollbackOn = { RuntimeException.class, Exception.class, SQLException.class })
    @PostMapping(
            value = "/addNewUser",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "Create a new User.", notes = "Creates a new User in the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully created", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the creation of a new user.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> addNewUser(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "fname") String fname,
            @RequestParam(value = "lname") String lname,
            @RequestParam(value = "cellphone") String cellphone,
            @RequestParam(value = "password") String password) throws Exception {
        try {
            UserDto userDto = new UserDto(fname, lname, LocalDate.now(), password, email, cellphone);
            if (userDto == null) {
                throw new SQLException("Invalid input detected, cancelling the request");
            }
            LOGGER.info("[User Controller log] addNewUser method, input object {} ", userDto);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1/c1/addNewUser").toUriString());
            UserDto userResponse = userCRUDService.createNewUser(userDto);
            PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
            return ResponseEntity.created(uri).body(response);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] createNewUser method, Could not create the new user, with error {}", error.getMessage());
            throw new RuntimeException("[User Controller log] createNewUser method, failed to execute the request", error.getCause());
        }
    }

    //Verifies the user is valid and in the database
    @Transactional(rollbackOn = { RuntimeException.class, Exception.class, SQLException.class })
    @PostMapping("/login")
    @ApiOperation(value = "Logs a user in.", notes = "Logs a user in.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User login successful", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not log the user in.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized: invalid credentials provided.", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<Boolean>> login(
            @ApiParam(value = "Email of the user", example = "reynardengels@gmail.com", name = "email", required = true)
            @RequestParam String email,
            @RequestParam String password) throws Exception {
        try {
            if (!userCRUDService.userExistsByEmail(email)) {
                boolean userResponse = userCRUDService.userExistsByEmail(email);
                PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(false, userResponse);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            LOGGER.info("[User Controller log] login method, user email {} ", email);
            boolean userResponse = userCRUDService.loginUser(email, password);
            if (!userResponse) {
                PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(false, userResponse);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] login method, Could not authenticate the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Controller log] login method, failed to execute the request", error.getCause());
        }
    }

    //Deletes the user from the database and from spring security
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
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
        try {
            LOGGER.info("[User Controller log] deleteUser method, input id {} ", id);
            UserDto userDto = userCRUDService.getUserDtoById(id);
            String awsDeleteFolder = awsCRUDService.deleteFolderForUser(userDto.getEmail());
            LOGGER.info("[User Controller log] deleteUser method, from folder {}", awsDeleteFolder);
            int userResponse = userCRUDService.deleteUser(id);
            PhotoMemoriesResponse<Integer> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] deleteUser method, Could not delete the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Controller log] deleteUser method, failed to execute the request", error.getCause());
        }
    }

    //Updates a user in the database
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @PutMapping("/updateAccount/{id}")
    @ApiOperation(value = "Updates a user by id.", notes = "Changes a user from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve updating the user by id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found the user with this id", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> updateUser(
            @ApiParam(value = "The id of each user", example = "1", name = "id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "The user's new first name.", example = "Riaan", name = "firstName", required = true)
            @RequestParam("firstName") String firstName,
            @ApiParam(value = "The user's new last name.", example = "Koggelmander", name = "lastName", required = true)
            @RequestParam("lastName") String lastName,
            @ApiParam(value = "The user's new email.", example = "riankoggelmander@gmail.com", name = "email", required = true)
            @RequestParam("email") String email,
            @ApiParam(value = "The user's new phone number.", example = "0765543174", name = "phoneNumber", required = true)
            @RequestParam("phoneNumber") String phoneNumber) throws Exception {
        try {
            LOGGER.info("[User Controller log] updateUser method, input id {} ", id);

            if (userCRUDService.verifyUserByPhoneNumberAndEmail(phoneNumber, email)) {
                LOGGER.error("[User Controller log] updateUser method, phone number {} and email {} not unique", phoneNumber, email);
                throw new RuntimeException("[User Controller Error] updateUser method, phone number and email not unique");
            }

            UserDto userResponse = userCRUDService.updateUserDto(firstName, lastName, email, phoneNumber, id);
            PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] updateUser method, Could not update the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Controller log] updateUser method, failed to execute the request", error.getCause());
        }
    }

    //Verifies that a user exists in the database
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
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
        try {
            LOGGER.info("[User Controller log] userExists method, input id {} ", id);
            boolean userResponse = userCRUDService.userExists(id);
            PhotoMemoriesResponse<Boolean> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] userExists method, Could not verify that the user with id {} exists, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Controller log] userExists method, failed to execute the request", error.getCause());
        }
    }

    //Returns a user from the database
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
        try {
            LOGGER.info("[User Controller log] getUserById method, input id {} ", id);
            UserDto userResponse = userCRUDService.getUserDtoById(id);
            PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] getUserById method, Could not get the user with id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Controller log] getUserById method, failed to execute the request", error.getCause());
        }
    }

    //Returns a userDto from the database by given email
    @GetMapping("/getUserByEmail/{email}")
    @ApiOperation(value = "Checks if a photo exists based on their email.", notes = "Tries to fetch a photo by email from the DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 400, message = "Bad Request: could not resolve the search by email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 404, message = "Could not found a user with this email", response = PhotoMemoriesResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = PhotoMemoriesResponse.class)})
    public ResponseEntity<PhotoMemoriesResponse<UserDto>> getUserByEmail(
            @ApiParam(value = "The email of each user", example = "reynardengels@gmail.com", name = "email", required = true)
            @PathVariable("email") String email) throws SQLException {
        try {
            LOGGER.info("[User Controller log] getUserByEmail method, input email {} ", email);
            UserDto userResponse = userCRUDService.getUserDtoByEmail(email);
            PhotoMemoriesResponse<UserDto> response = new PhotoMemoriesResponse<>(true, userResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException error) {
            LOGGER.error("[User Controller log] getUserByEmail method, Could not get the user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Controller log] getUserByEmail method, failed to execute the request", error.getCause());
        }
    }
}
