package com.photomemories.logic.flow;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.UserCRUDService;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("userServiceFlow")
public class UserCRUDServiceImpl implements UserCRUDService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoCRUDServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserTranslator userTranslator;

    @Autowired
    public UserCRUDServiceImpl(UserTranslator userTranslator) {
        this.userTranslator = userTranslator;
    }

    @Override
    public UserDto createNewUser(UserDto userDto) throws Exception {
        try {
            LOGGER.info("[User Logic log] input Dto object is: {}", userDto);

            isUniqueUser(userDto);

            User user = userDto.buildUser();
            LOGGER.info("[User Logic log] Dto Object converted to persistence is: {}", user);
            user.setUserHashPassword(passwordEncoder.encode(userDto.getUserHashPassword()));
            user.setDate(LocalDate.now());
            User addedUser = userTranslator.newUser(user);
            LOGGER.info("[USer Logic log] object saved to database: {}", addedUser);
            UserDto returnUser = new UserDto(userTranslator.newUser(addedUser));
            LOGGER.info("[User Logic log] Dto returned: {}", returnUser);
            return returnUser;
        } catch (Exception e) {
            throw new RuntimeException("User could not be created!", e);
        }
    }

    @Override
    public UserDto getUserDtoById(Integer id) {
        return new UserDto(userTranslator.getUserById(id));
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        if (!userTranslator.userExistsWithEmail(email)) {
            throw new RuntimeException("User with email " + email + " does not exist");
        }
        return new UserDto(userTranslator.getUserByEmail(email));
    }

    private void isUniqueUser(UserDto userDto) throws Exception {
        if (userTranslator.registerCheck(userDto.getPhoneNumber(), userDto.getEmail())) {
            LOGGER.warn("User with phone number: {} and email: {} already exists", userDto.getPhoneNumber(), userDto.getEmail());
            throw new RuntimeException("User already exists");
        }
    }

    @Override
    public boolean userExists(Integer id) {
        LOGGER.info("[User Logic log] userExists method, queried id: {}", id);
        boolean returnLogicValue = userTranslator.userExists(id);
        LOGGER.info("[User Logic log] userExists method, result: {}", returnLogicValue);
        return returnLogicValue;
    }

    @Override
    public Integer deleteUser(Integer id) throws Exception {
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
    }

    @Override
    public boolean loginUser(String password, String email) throws Exception {
        LOGGER.info("[User Logic log] loginUser method, password: {} and email: {}", password, email);
        boolean userValid = userTranslator.loginUser(password, email);
        LOGGER.info("[User Logic log] loginUser method, (valid?): {}", userValid);
        return userValid;
    }
}
