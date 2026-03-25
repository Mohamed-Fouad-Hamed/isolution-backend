package com.alf.auth.mappers;

import com.alf.core_common.dtos.user.AccountRegister;
import com.alf.core_common.dtos.user.SignUpDto;
import com.alf.core_common.dtos.user.UserDto;
import com.alf.core_common.dtos.user.UserSignUpByAccount;
import com.alf.auth.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toUserDto(User user);

    @Mapping(target = "password",ignore = true)
    User signUpToUser(SignUpDto signUpDto);

    @Mapping(target = "password",ignore = true)
    User signUpToUserByAccount(UserSignUpByAccount userSignUpByAccount);

    @Mapping(target = "password",ignore = true)
    User accountSignUpToUser(AccountRegister accountRegister);

}

