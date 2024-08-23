package dev.jlkeesh.validator;

import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.utils.StringUtil;

public final class UserValidator {
    private UserValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void isValidate(UserCreateDto dto) {
        if (StringUtil.isBlank(dto.username())) {
            throw new ServiceException("Username cannot be empty", 400);
        }
        if (StringUtil.isBlank(dto.password())) {
            throw new ServiceException("Password cannot be empty", 400);
        }
        if (StringUtil.isBlank(dto.email())) {
            throw new ServiceException("Email cannot be empty", 400);
        }
        if (dto.authRole() == null) {
            throw new ServiceException("Role cannot be null", 400);
        }
    }
}
