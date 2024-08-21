package dev.jlkeesh.mapper.app;

import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.UserCreateDto;
import dev.jlkeesh.dto.UserDto;
import dev.jlkeesh.dto.UserUpdateDto;
import dev.jlkeesh.utils.PasswordUtil;

import java.util.List;

public class UserMapper {
    public UserDto toDto(User user) {
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

    public List<UserDto> toDto(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    public User fromCreateDto(UserCreateDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setRole(dto.authRole());
        return user;
    }

    public User fromUpdateDto(User user, UserUpdateDto dto) {
        if (dto.username() != null) {
            user.setUsername(dto.username());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.authRole() != null) {
            user.setRole(dto.authRole());
        }
        return user;
    }
}
