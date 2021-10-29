package com.photomemories.translator;

import com.photomemories.domain.persistence.User;
import org.springframework.stereotype.Component;

@Component
public interface UserTranslator {
    User newUser(User user) throws Exception;

    User getUserById(Integer id);

    User getUserByEmail(String email);

    boolean userExistsWithEmail(String email);

    boolean userExists(Integer id);

    Integer deleteUser(Integer id) throws Exception;

    //TODO: Update user method

    boolean loginUser(String password, String email) throws Exception;

    boolean registerCheck(String phoneNumber, String email) throws Exception;
}
