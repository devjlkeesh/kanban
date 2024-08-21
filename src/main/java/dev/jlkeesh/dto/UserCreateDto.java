package dev.jlkeesh.dto;

import dev.jlkeesh.enums.AuthRole;

public record UserCreateDto(String username, String email, String password, AuthRole authRole) implements Dto {
}
