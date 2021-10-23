package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.User;
import com.photomemories.repo.persistence.UserRepository;
import com.photomemories.translator.UserTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTranslatorImpl implements UserTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTranslatorImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserTranslatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User newUser(User user) throws Exception {
        LOGGER.info("[User Translator log] newUser method, input object: {}", user);
        return userRepository.save(user);
    }

    @Override
    public boolean userExists(Integer id) {
        LOGGER.info("[User Translator log] userExists method, input id: {}", id);
        boolean returnValue = userRepository.existsByUserId(id);
        LOGGER.info("[User Translator log] userExists method, result: {}", returnValue);
        return returnValue;
    }

    //TODO: Delete method
    //TODO: Update method
    //TODO: Get by email method
}
