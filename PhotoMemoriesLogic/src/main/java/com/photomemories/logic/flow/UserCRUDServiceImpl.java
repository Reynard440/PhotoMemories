package com.photomemories.logic.flow;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.UserCRUDService;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Component("userServiceFlow")
public class UserCRUDServiceImpl implements UserCRUDService, UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoCRUDServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserTranslator userTranslator;

    //Dependency Injection: userTranslator and passwordEncoder are injected and singleton pattern is followed
    @Autowired
    public UserCRUDServiceImpl(UserTranslator userTranslator, PasswordEncoder passwordEncoder) {
        this.userTranslator = userTranslator;
        this.passwordEncoder = passwordEncoder;
    }

    //Inserts a userDto in the database
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public UserDto createNewUser(UserDto userDto) throws SQLException, Exception {
        try {
            LOGGER.info("[User Logic log] createNewUser method, input Dto object is {}", userDto);

            isUniqueUser(userDto);

            User user = userDto.buildUser();
            LOGGER.info("[User Logic log] createNewUser method, Dto Object converted to persistence is {}", user);

            user.setUserHashPassword(passwordEncoder.encode(userDto.getUserHashPassword()));
            user.setDate(LocalDate.now());

            User addedUser = userTranslator.newUser(user);
            LOGGER.info("[USer Logic log] createNewUser method, object saved to database {}", addedUser);

            UserDto returnUser = new UserDto(userTranslator.newUser(addedUser));
            LOGGER.info("[User Logic log] createNewUser method, Dto returned {}", returnUser);

            return returnUser;
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] createNewUser method, Could not create the new user Dto, with error {}", error.getMessage());
            throw new RuntimeException("[User Logic log] createNewUser method, failed to execute the request", error.getCause());
        }
    }

    //Deletes a userDto from the database with given id
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Integer deleteUser(Integer id) throws SQLException {
        try {
            LOGGER.info("[User Logic log] deleteUser method, queried id: {}", id);
            boolean beforeDelete = userTranslator.userExists(id);
            LOGGER.info("[User Logic log] deleteUser method, (exists?): {}", beforeDelete);

            if (!userExists(id)) {
                LOGGER.warn("User with id {} does not exists", id);
                throw new RuntimeException("User deletion error.");
            }

            int userDelete = userTranslator.deleteUser(id);
            boolean afterDelete = userTranslator.userExists(id);
            LOGGER.info("[User Logic log] deleteUser method, (exists?): {}", afterDelete);
            return userDelete;
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] deleteUser method, Could not delete the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Logic log] deleteUser method, failed to execute the request", error.getCause());
        }
    }

    //Updates a userDto in the database with given id and information
    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public UserDto updateUserDto(String firstName, String lastName, String email, String phoneNumber, Integer userId) throws SQLException {
        try {
            int returnValue = userTranslator.updateUser(firstName, lastName, email, phoneNumber, userId);
            if (returnValue == 0) {
                LOGGER.error("[User Logic log] updateUserDto method, did not update account: {}", false);
                throw new RuntimeException("[User Logic Error] updateUserDto method, did not update account");
            }
            return new UserDto(userTranslator.getUserById(userId));
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] updateUserDto method, Could not update the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Logic log] updateUserDto method, failed to execute the request", error.getCause());
        }
    }

    //Returns a userDto object from the database with given id
    @Override
    public UserDto getUserDtoById(Integer id) {
        try {
            LOGGER.info("[User Logic log] getUserDtoById method, input id {}", id);
            return new UserDto(userTranslator.getUserById(id));
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] getUserDtoById method, Could not get the user with id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Logic log] getUserDtoById method, failed to execute the request", error.getCause());
        }
    }

    //Returns a userDto object from the database with given email
    @Override
    public UserDto getUserDtoByEmail(String email) {
        try {
            LOGGER.info("[User Logic log] getUserDtoById method, input email {}", email);
            if (!userTranslator.userExistsWithEmail(email)) {
                LOGGER.error("[User Logic log] getUserDtoByEmail method, User with email {} does not exist", email);
                throw new UserNotFoundException("User with email " + email + " does not exist");
            }
            return new UserDto(userTranslator.getUserByEmail(email));
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] getUserDtoByEmail method, Could not get the userDto with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Logic log] getUserDtoByEmail method, failed to execute the request", error.getCause());
        }
    }

    //Private method that verifies that the userDto is unique
    private void isUniqueUser(UserDto userDto) throws Exception {
        if (userTranslator.registerCheck(userDto.getPhoneNumber(), userDto.getEmail())) {
            LOGGER.warn("User with phone number: {} and email: {} already exists", userDto.getPhoneNumber(), userDto.getEmail());
            throw new RuntimeException("User already exists");
        }
    }

    //Confirms that the userDto exists with email or not
    @Override
    public boolean userExistsByEmail(String email) {
        try {
            LOGGER.info("[User Logic log] userExists method, queried email {}", email);
            boolean returnLogicValue = userTranslator.userExistsWithEmail(email);
            LOGGER.info("[User Logic log] userExists method, result {}", returnLogicValue);
            return returnLogicValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] userExistsByEmail method, Could not verify the user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Logic log] userExistsByEmail method, failed to execute the request", error.getCause());
        }
    }

    //Confirms that the user exists by id or not
    @Override
    public boolean userExists(Integer id) {
        try {
            LOGGER.info("[User Logic log] userExists method, queried id: {}", id);
            boolean returnLogicValue = userTranslator.userExists(id);
            LOGGER.info("[User Logic log] userExists method, result: {}", returnLogicValue);
            return returnLogicValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] userExists method, Could not verify the user with id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Logic log] userExists method, failed to execute the request", error.getCause());
        }
    }

    //Confirms that the login of a user
    @Override
    public boolean loginUser(String password, String email) throws Exception {
        try {
            LOGGER.info("[User Logic log] loginUser method, password: {} and email: {}", password, email);
            boolean userValid = userTranslator.loginUser(password, email);
            LOGGER.info("[User Logic log] loginUser method, (valid?): {}", userValid);
            return userValid;
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] loginUser method, Could not log the user in, with error {}", error.getMessage());
            throw new RuntimeException("[User Logic log] loginUser method, failed to execute the request", error.getCause());
        }
    }

    //Verifies that the userDto is unique according to given parameters
    @Override
    public boolean verifyUserByPhoneNumberAndEmail(String phoneNumber, String email) throws Exception {
        try {
            return userTranslator.registerCheck(phoneNumber, email);
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] verifyUserByPhoneNumberAndEmail method, Could not verify the user with phone number {} and email {}, with error {}", phoneNumber, email, error.getMessage());
            throw new RuntimeException("[User Logic log] verifyUserByPhoneNumberAndEmail method, failed to execute the request", error.getCause());
        }
    }

    //Method that loads the user by given email, implemented for spring security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserDto userDto = new UserDto(userTranslator.getUserByEmail(email));
            if (userDto == null) {
                LOGGER.error("User not present in database");
                throw new UsernameNotFoundException("User not present in database");
            } else {
                LOGGER.info("User present in database: {}", email);
            }
            Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("USER_ROLE"));
            return new org.springframework.security.core.userdetails.User(userDto.getEmail(), userDto.getUserHashPassword(), auths);
        } catch (RuntimeException error) {
            LOGGER.error("[User Logic log] loadUserByUsername method, Could not load the user with email {} by email, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Logic log] loadUserByUsername method, failed to execute the request", error.getCause());
        }
    }
}
