package dev.jlkeesh.service;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dto.UserCreateDto;
import dev.jlkeesh.dto.UserDto;
import dev.jlkeesh.dto.UserUpdateDto;

public interface UserService extends GenericCrudService<UserDto,
        UserCreateDto, UserUpdateDto, UserCriteria, Long> {
}
