package com.photomemories.translator;

import com.photomemories.domain.persistence.User;
import org.springframework.stereotype.Component;

@Component
public interface UserTranslator {
    User newUser(User user) throws Exception;

    boolean userExists(Integer id);
}
