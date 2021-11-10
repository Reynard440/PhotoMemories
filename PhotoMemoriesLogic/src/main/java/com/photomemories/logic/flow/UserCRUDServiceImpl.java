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

    @Autowired
    public UserCRUDServiceImpl(UserTranslator userTranslator, PasswordEncoder passwordEncoder) {
        this.userTranslator = userTranslator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createNewUser(UserDto userDto) throws Exception {
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
        } catch (Exception e) {
            LOGGER.warn("[User Logic log] createNewUser method, exception with error {}", e.getMessage());
            throw new RuntimeException("User could not be created!", e);
        }
    }

    @Override
    public UserDto getUserDtoById(Integer id) {
        LOGGER.info("[User Logic log] getUserDtoById method, input id {}", id);
        return new UserDto(userTranslator.getUserById(id));
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        LOGGER.info("[User Logic log] getUserDtoById method, input email {}", email);
        if (!userTranslator.userExistsWithEmail(email)) {
            LOGGER.error("[User Logic log] getUserDtoByEmail method, User with email {} does not exist", email);
            throw new UserNotFoundException("User with email " + email + " does not exist");
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
    public boolean userExistsByEmail(String email) {
        LOGGER.info("[User Logic log] userExists method, queried email {}", email);
        boolean returnLogicValue = userTranslator.userExistsWithEmail(email);
        LOGGER.info("[User Logic log] userExists method, result {}", returnLogicValue);
        return returnLogicValue;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
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

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public UserDto updateUserDto(String firstName, String lastName, String email, String phoneNumber, Integer userId) {
        int returnValue = userTranslator.updateUser(firstName, lastName, email, phoneNumber, userId);

        if (returnValue == 0) {
            LOGGER.error("[User Logic log] updateUserDto method, did not update account: {}", false);
            throw new RuntimeException("[User Logic Error] updateUserDto method, did not update account");
        }

        return new UserDto(userTranslator.getUserById(userId));
    }

    @Override
    public boolean loginUser(String password, String email) throws Exception {
        LOGGER.info("[User Logic log] loginUser method, password: {} and email: {}", password, email);
        boolean userValid = userTranslator.loginUser(password, email);
        LOGGER.info("[User Logic log] loginUser method, (valid?): {}", userValid);
        return userValid;
    }

    @Override
    public boolean verifyUserByPhoneNumberAndEmail(String phoneNumber, String email) throws Exception {
        return userTranslator.registerCheck(phoneNumber, email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
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
    }
}
