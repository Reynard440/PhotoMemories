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

    @Autowired
    public UserTranslatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public User newUser(User user) throws Exception {
        LOGGER.info("[User Translator log] newUser method, input object's email: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        LOGGER.info("[User Translator log] getUserById method, input id: {}", id);
        return userRepository.findByUserId(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        if (!userExistsWithEmail(email)) {
            LOGGER.warn("[User Translator log] getUserByEmail method, User with email {} does not exist", email);
            throw new RuntimeException("User with email " + email + " does not exist");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean userExistsWithEmail(String email) {
        boolean returnValue = userRepository.existsByEmail(email);
        LOGGER.info("[User Translator log] userExistsWithEmail method, email exists: {}", returnValue);
        return returnValue;
    }

    @Override
    public boolean userExists(Integer id) {
        LOGGER.info("[User Translator log] userExists method, input id: {}", id);
        boolean returnValue = userRepository.existsByUserId(id);
        LOGGER.info("[User Translator log] userExists method, result: {}", returnValue);
        return returnValue;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Integer deleteUser(Integer id) throws Exception {
        LOGGER.info("[User Translator log] deleteUser method, input id: {}", id);
        int deleteValue = userRepository.deleteByUserId(id);
        LOGGER.info("[User Translator log] deleteUser method, (exists?): {}", deleteValue);
        return deleteValue;
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class})
    @Override
    public Integer updateUser(String firstName, String lastName, String email, String phoneNumber, Integer userId) {
        return userRepository.updateUser(firstName, lastName, email, phoneNumber, userId);
    }

    @Override
    public boolean loginUser(String password, String email) throws Exception {
        LOGGER.info("[User Translator log] loginUser method, input password: {}, and email: {}", password, email);
        boolean loginValue = userRepository.existsByUserHashPasswordAndEmail(password, email);
        LOGGER.info("[User Translator log] loginUser method, (exists?): {}", loginValue);
        return loginValue;
    }

    @Override
    public boolean registerCheck(String phoneNumber, String email) throws Exception {
        LOGGER.info("[User Translator log] registerCheck method, input phone number: {} and email: {}", phoneNumber, email);
        if (userRepository.existsByPhoneNumberAndEmail(phoneNumber, email)) {
            LOGGER.error("[User Translator log] registerCheck method, user with phone number: {} and email: {} already exists", phoneNumber, email);
           throw new RuntimeException("[User Translator Error] registerCheck method, user already exists");
        }
        LOGGER.info("[User Translator log] registerCheck method, user status: {}", false);
        return false;
    }
}
