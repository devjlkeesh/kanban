package dev.jlkeesh.service.impl;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.auth.LoginDto;
import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.dto.user.UserDto;
import dev.jlkeesh.dto.user.UserUpdateDto;
import dev.jlkeesh.enums.AuthRole;
import dev.jlkeesh.exception.NotFoundException;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.mapper.app.UserMapper;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.AESUtil;
import dev.jlkeesh.utils.PasswordUtil;
import dev.jlkeesh.validator.UserValidator;
import lombok.NonNull;

import java.util.List;

public class RestUserService implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;

    public RestUserService(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
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
                .orElseThrow(() -> new NotFoundException("user not found. id: " + id));
    }

    @Override
    public Long create(@NonNull UserCreateDto dto) {
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
        Long userId = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        AuthRole userRole = user.getRole();
        String inputData = userId + ":" + username + ":" + email + ":" + userRole;
        String token = AESUtil.encrypt(inputData);
        return token;
    }
}
