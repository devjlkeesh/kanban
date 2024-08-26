package dev.jlkeesh.validator;

import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

public final class UserValidator {
    private UserValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void isValidate(UserCreateDto dto) {
        if (StringUtils.isBlank(dto.username())) {
            throw new ServiceException("Username cannot be empty", 400);
        }
        if (StringUtils.isBlank(dto.password())) {
            throw new ServiceException("Password cannot be empty", 400);
        }
        if (StringUtils.isBlank(dto.email())) {
            throw new ServiceException("Email cannot be empty", 400);
        }
        if (dto.authRole() == null) {
            throw new ServiceException("Role cannot be null", 400);
        }
    }
}
