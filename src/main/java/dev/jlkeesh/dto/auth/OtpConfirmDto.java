package dev.jlkeesh.dto.auth;

public record OtpConfirmDto(String code, String newPassword, String confirmPassword) {
}
