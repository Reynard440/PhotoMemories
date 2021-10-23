package com.photomemories.logic;

import com.photomemories.domain.dto.UserDto;

public interface UserCRUDService {
    UserDto createNewUser(UserDto userDto) throws Exception;

    boolean userExists(Integer id);
}
