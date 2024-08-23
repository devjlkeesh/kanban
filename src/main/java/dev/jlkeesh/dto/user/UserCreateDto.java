package dev.jlkeesh.dto.user;

import dev.jlkeesh.dto.Dto;
import dev.jlkeesh.enums.AuthRole;

public record UserCreateDto(String username, String email, String password, AuthRole authRole) implements Dto {
}
