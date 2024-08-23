package dev.jlkeesh.dto.auth;

import dev.jlkeesh.enums.AuthRole;

public record SessionUserDto(Long userId,
                             AuthRole userRole,
                             String username,
                             String email) {
}
