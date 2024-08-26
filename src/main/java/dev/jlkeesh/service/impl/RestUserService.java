package dev.jlkeesh.service.impl;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dao.OtpDao;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.Otp;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.auth.LoginDto;
import dev.jlkeesh.dto.auth.OtpConfirmDto;
import dev.jlkeesh.dto.auth.OtpSendDto;
import dev.jlkeesh.dto.auth.UserSessionDto;
import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.dto.user.UserDto;
import dev.jlkeesh.dto.user.UserUpdateDto;
import dev.jlkeesh.enums.AuthRole;
import dev.jlkeesh.exception.NotFoundException;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.mapper.app.UserMapper;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.AESUtil;
import dev.jlkeesh.utils.DigitUtil;
import dev.jlkeesh.utils.PasswordUtil;
import dev.jlkeesh.utils.UserSession;
import dev.jlkeesh.validator.UserValidator;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class RestUserService implements UserService {
    private final UserDao userDao;
    private final OtpDao otpDao;
    private final UserMapper userMapper;
    private final MailService mailService;

    public RestUserService(UserDao userDao, OtpDao otpDao, UserMapper userMapper, MailService mailService) {
        this.userDao = userDao;
        this.otpDao = otpDao;
        this.userMapper = userMapper;
        this.mailService = mailService;
    }

    @Override
    public List<UserDto> getAll(@NonNull UserCriteria criteria) {
        List<User> users = userDao.findAll();
        return userMapper.toDto(users);
    }

    @Override
    public UserDto get(@NonNull Long id) {
        User user = requireUser(id);
        return userMapper.toDto(user);
    }

    private User requireUser(@NonNull Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new ServiceException("user not found. id: " + id, 404));
    }

    @Override
    public Long create(@NonNull UserCreateDto dto) {
        UserSessionDto userSessionDto = UserSession.get();
        if (!AuthRole.ADMIN.equals(userSessionDto.userRole())) {
            throw new ServiceException("access denied", 403);
        }
        UserValidator.isValidate(dto);
        if (userDao.findByUsername(dto.username()).isPresent()) {
            throw new ServiceException("username already exists", 400);
        }
        if (userDao.findByEmail(dto.email()).isPresent()) {
            throw new ServiceException("email already exists", 400);
        }
        User user = userMapper.fromCreateDto(dto);
        user.setPassword(PasswordUtil.hash(dto.password()));
        user = userDao.save(user);
        return user.getId();
    }

    @Override
    public boolean update(@NonNull Long id, @NonNull UserUpdateDto dto) {
        User user = requireUser(id);
        user = userMapper.fromUpdateDto(dto, user);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        User user = requireUser(id);
        userDao.delete(user);
        return true;
    }

    @Override
    public String login(@NonNull LoginDto dto) {
        User user = userDao.findByEmailOrUsername(dto.subject())
                .orElseThrow(() -> new ServiceException("unauthorized", 401));
        if (!PasswordUtil.checkPassword(dto.password(), user.getPassword())) {
            //TODO bu yerda login try count ni oshirish kerak
            throw new ServiceException("unauthorized", 400);
        }
        String stringToEncrypt = StringUtils.joinWith(":",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());
        return AESUtil.encrypt(stringToEncrypt);
    }

    @Override
    public boolean otpSend(OtpSendDto dto) {
        User user = userDao.findByEmail(dto.email())
                .orElseThrow(() -> new ServiceException("user not found.", 404));
        Otp otp = new Otp();
        otp.setUserId(user.getId());
        String code = String.valueOf(DigitUtil.generateNumber(8));
        otp.setCode(code);
        otpDao.save(otp);
        mailService.sendOtp(code, user.getEmail());
        return true;
    }

    @Override
    public boolean otpConfirm(OtpConfirmDto dto) {
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new ServiceException("password mismatch", 400);
        }
        Otp otp = otpDao.findByCodeAndNotUsed(dto.code())
                .orElseThrow(() -> new ServiceException("otp not found", 400));

        if (otp.getValidTill().isBefore(LocalDateTime.now())) {
            throw new ServiceException("otp expired", 400);
        }
        Long userId = otp.getUserId();
        User user = requireUser(userId);
        userDao.resetPassword(userId, dto.newPassword());
        otp.setUsed(true);
        //TODO update yozish kerak
        otpDao.update(otp);
        return true;
    }
}
