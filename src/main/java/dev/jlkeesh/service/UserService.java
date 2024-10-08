package dev.jlkeesh.service;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dto.auth.LoginDto;
import dev.jlkeesh.dto.auth.OtpConfirmDto;
import dev.jlkeesh.dto.auth.OtpSendDto;
import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.dto.user.UserDto;
import dev.jlkeesh.dto.user.UserUpdateDto;

public interface UserService extends GenericCrudService<UserDto,
        UserCreateDto, UserUpdateDto, UserCriteria, Long> {
    String login(LoginDto dto);

    boolean otpSend(OtpSendDto dto);

    boolean otpConfirm(OtpConfirmDto dto);
}
