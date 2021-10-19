package com.photomemories.translator.impl;

import com.photomemories.domain.persistence.User;
import com.photomemories.repo.persistence.UserRepository;
import com.photomemories.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTranslatorImpl implements UserTranslator {
    private final UserRepository userRepository;

    @Autowired
    public UserTranslatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User newUser(User user) throws Exception {
        return userRepository.save(user);
    }
}
