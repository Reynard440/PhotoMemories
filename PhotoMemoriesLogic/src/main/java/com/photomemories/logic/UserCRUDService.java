package com.photomemories.logic;

import com.photomemories.domain.dto.UserDto;
import com.photomemories.domain.persistence.User;

public interface UserCRUDService {

    UserDto createNewUser(UserDto userDto) throws Exception;
}
