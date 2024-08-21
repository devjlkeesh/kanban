package dev.jlkeesh.mapper.app;

import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.UserDto;

import java.util.List;

public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getUpdatedAt(),
                user.getUpdatedBy(),
                user.getRole()
        );
    }

    public List<UserDto> toUserDto(List<User> users) {
        return users.stream().map(this::toUserDto).toList();
    }

}
