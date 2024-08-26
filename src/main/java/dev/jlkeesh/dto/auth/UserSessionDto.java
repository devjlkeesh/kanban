package dev.jlkeesh.dto.auth;

import dev.jlkeesh.enums.AuthRole;

public record UserSessionDto(Long userId,
                             String username,
                             String email,
                             AuthRole userRole
) {
}
