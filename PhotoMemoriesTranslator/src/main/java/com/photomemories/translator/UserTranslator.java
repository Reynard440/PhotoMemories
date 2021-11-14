package com.photomemories.translator;

import com.photomemories.domain.persistence.User;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public interface UserTranslator {
    User newUser(User user) throws Exception;

    Integer deleteUser(Integer id) throws Exception;

    Integer updateUser(String firstName, String lastName, String email, String phoneNumber, Integer userId) throws SQLException;

    User getUserById(Integer id);

    User getUserByEmail(String email);

    boolean userExistsWithEmail(String email);

    boolean userExists(Integer id);

    boolean loginUser(String password, String email) throws Exception;

    boolean registerCheck(String phoneNumber, String email) throws Exception;
}
