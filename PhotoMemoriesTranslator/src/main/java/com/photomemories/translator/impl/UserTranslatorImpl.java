package com.photomemories.translator.impl;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.photomemories.domain.persistence.User;
import com.photomemories.repo.persistence.UserRepository;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Component
public class UserTranslatorImpl implements UserTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTranslatorImpl.class);
    private final UserRepository userRepository;

    //Dependency Injection: userRepository is injected and singleton pattern is followed
    @Autowired
    public UserTranslatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Inserts a user in the database
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public User newUser(User user) throws SQLException {
        try {
            LOGGER.info("[User Translator log] newUser method, input object's email: {}", user.getEmail());
            return userRepository.save(user);
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] newUser method, Could not add the new user, with error {}", error.getMessage());
            throw new RuntimeException("[User Translator Error] newUser method, failed to execute the request ", error.getCause());
        }
    }

    //Deletes a user from the database with given id
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Integer deleteUser(Integer id) throws SQLException {
        try {
            LOGGER.info("[User Translator log] deleteUser method, input id: {}", id);
            int deleteValue = userRepository.deleteByUserId(id);
            LOGGER.info("[User Translator log] deleteUser method, (exists?): {}", deleteValue);
            return deleteValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] deleteUser method, Could not delete the user with id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Translator Error] deleteUser method, failed to execute the request ", error.getCause());
        }
    }

    //Updates a user in the database with given id and information
    @Transactional(rollbackOn = {SQLException.class, Exception.class, RuntimeException.class})
    @Override
    public Integer updateUser(String firstName, String lastName, String email, String phoneNumber, Integer userId) throws SQLException {
        try {
            return userRepository.updateUser(firstName, lastName, email, phoneNumber, userId);
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] newUser method, Could not update the user, with error {}", error.getMessage());
            throw new RuntimeException("[User Translator Error] newUser method, failed to execute the request ", error.getCause());
        }
    }

    //Returns a user object from the database with given id
    @Override
    public User getUserById(Integer id) {
        try {
            LOGGER.info("[User Translator log] getUserById method, input id: {}", id);
            return userRepository.findByUserId(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] getUserById method, Could not get the user by id {}, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Translator Error] getUserById method, failed to execute the request ", error.getCause());
        }
    }

    //Returns a user object from the database with given email
    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        try {
            if (!userExistsWithEmail(email)) {
                LOGGER.warn("[User Translator log] getUserByEmail method, User with email {} does not exist", email);
                throw new UserNotFoundException("User with email " + email + " does not exist");
            }
            return userRepository.findByEmail(email);
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] getUserByEmail method, Could not find the user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Translator Error] getUserByEmail method, failed to execute the request ", error.getCause());
        }
    }

    //Confirms that the user exists with email or not
    @Override
    public boolean userExistsWithEmail(String email) {
        try {
            boolean returnValue = userRepository.existsByEmail(email);
            LOGGER.info("[User Translator log] userExistsWithEmail method, email exists: {}", returnValue);
            return returnValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] userExistsWithEmail method, Could not confirm if the user with email {} exist, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Translator Error] userExistsWithEmail method, failed to execute the request ", error.getCause());
        }
    }

    //Confirms that the user exists by id or not
    @Override
    public boolean userExists(Integer id) {
        try {
            boolean returnValue = userRepository.existsByUserId(id);
            LOGGER.info("[User Translator log] userExists method, result: {}", returnValue);
            return returnValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] userExists method, Could not confirm if the user with id {} exists, with error {}", id, error.getMessage());
            throw new RuntimeException("[User Translator Error] userExists method, failed to execute the request ", error.getCause());
        }
    }

    //Confirms that the login of a user
    @Override
    public boolean loginUser(String password, String email) throws Exception {
        try {
            LOGGER.info("[User Translator log] loginUser method, input email: {}", email);
            boolean loginValue = userRepository.existsByUserHashPasswordAndEmail(password, email);
            LOGGER.info("[User Translator log] loginUser method, (exists?): {}", loginValue);
            return loginValue;
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] loginUser method, Could not confirm the login of the user with email {}, with error {}", email, error.getMessage());
            throw new RuntimeException("[User Translator Error] loginUser method, failed to execute the request ", error.getCause());
        }
    }

    //Confirms that the user is not already registered
    @Override
    public boolean registerCheck(String phoneNumber, String email) throws Exception {
        try {
            LOGGER.info("[User Translator log] registerCheck method, input phone number: {} and email: {}", phoneNumber, email);
            if (userRepository.existsByPhoneNumberAndEmail(phoneNumber, email)) {
                LOGGER.error("[User Translator log] registerCheck method, user with phone number: {} and email: {} already exists", phoneNumber, email);
                throw new RuntimeException("[User Translator Error] registerCheck method, user already exists");
            }
            LOGGER.info("[User Translator log] registerCheck method, user status: {}", false);
            return false;
        } catch (RuntimeException error) {
            LOGGER.error("[User Translator log] registerCheck method, Could not verify that the user is unique with email {} and phone number {}, with error {}", email, phoneNumber, error.getMessage());
            throw new RuntimeException("[User Translator Error] registerCheck method, failed to execute the request ", error.getCause());
        }
    }
}
