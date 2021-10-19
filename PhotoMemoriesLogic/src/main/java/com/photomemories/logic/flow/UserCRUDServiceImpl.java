package com.photomemories.logic.flow;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.User;
import com.photomemories.logic.UserCRUDService;
import com.photomemories.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("userServiceFlow")
public class UserCRUDServiceImpl implements UserCRUDService {
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
            User user = userDto.buildUser();
            user.setUserHashPassword(passwordEncoder.encode(userDto.getUserHashPassword()));
            user.setDate(LocalDate.now());
            User addedUser = userTranslator.newUser(user);
            return new UserDto(userTranslator.newUser(addedUser));
        } catch (Exception e) {
            throw new RuntimeException("User could not be created!", e);
        }
    }
}
