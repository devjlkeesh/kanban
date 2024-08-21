package dev.jlkeesh.dto;

import dev.jlkeesh.enums.AuthRole;

public record UserUpdateDto(String username, String email, AuthRole authRole) implements Dto {
}
