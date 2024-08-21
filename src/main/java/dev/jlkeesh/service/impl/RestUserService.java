package dev.jlkeesh.service.impl;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.UserCreateDto;
import dev.jlkeesh.dto.UserDto;
import dev.jlkeesh.dto.UserUpdateDto;
import dev.jlkeesh.exception.NotFoundException;
import dev.jlkeesh.mapper.app.UserMapper;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.PasswordUtil;
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
        User user = userMapper.fromCreateDto(dto);
        user.setPassword(PasswordUtil.hash(dto.password()));
        user = userDao.save(user);
        return user.getId();
    }

    @Override
    public boolean update(@NonNull Long id, @NonNull UserUpdateDto dto) {
        User user = requireUser(id);
        user = userMapper.fromUpdateDto(user, dto);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        User user = requireUser(id);
        userDao.delete(user);
        return true;
    }
}
