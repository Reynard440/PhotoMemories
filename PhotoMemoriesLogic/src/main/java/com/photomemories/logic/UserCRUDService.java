package com.photomemories.logic;

import com.photomemories.domain.dto.UserDto;

public interface UserCRUDService {
    UserDto createNewUser(UserDto userDto) throws Exception;

    UserDto getUserDtoById(Integer id);

    boolean userExists(Integer id);

    Integer deleteUser(Integer id) throws Exception;

    boolean loginUser(String password, String email) throws Exception;
}
