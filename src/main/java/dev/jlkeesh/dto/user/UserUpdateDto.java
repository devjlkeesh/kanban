package dev.jlkeesh.dto.user;

import dev.jlkeesh.dto.Dto;
import dev.jlkeesh.enums.AuthRole;

public record UserUpdateDto(String username, String email, AuthRole authRole) implements Dto {
}
