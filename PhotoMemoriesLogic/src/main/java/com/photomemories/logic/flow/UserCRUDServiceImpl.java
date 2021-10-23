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

    //TODO: when logging a user in, use the following to verify the password passwordEncoder.matches(userDto.getUserHashPassword(), user.getUserHashPassword()
    @Override
    public UserDto createNewUser(UserDto userDto) throws Exception {
        try {
            LOGGER.info("[User Logic log] input Dto object is: {}", userDto);
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
    public boolean userExists(Integer id) {
        LOGGER.info("[User Logic log] userExists method, queried id: {}", id);
        boolean returnLogicValue = userTranslator.userExists(id);
        LOGGER.info("[User Logic log] userExists method, result: {}", returnLogicValue);
        return returnLogicValue;
    }
}
