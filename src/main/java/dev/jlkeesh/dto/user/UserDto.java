package dev.jlkeesh.dto.user;

import dev.jlkeesh.dto.Dto;
import dev.jlkeesh.enums.AuthRole;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt,
        Long createdBy,
        LocalDateTime updatedAt,
        Long updatedBy,
        AuthRole role) implements Dto {
}
