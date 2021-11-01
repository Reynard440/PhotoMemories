package com.photomemories.logic;

import com.photomemories.domain.dto.UserDto;

public interface UserCRUDService {
    UserDto createNewUser(UserDto userDto) throws Exception;

    UserDto getUserDtoById(Integer id);

    UserDto getUserDtoByEmail(String email);

    boolean userExists(Integer id);

    Integer deleteUser(Integer id) throws Exception;

    UserDto updateUserDto(String firstName, String lastName, String email, String phoneNumber, Integer userId);

    boolean loginUser(String password, String email) throws Exception;

    boolean verifyUserByPhoneNumberAndEmail (String phoneNumber, String email) throws Exception;
}
