package dev.jlkeesh.service.impl;

import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.dto.UserCreateDto;
import dev.jlkeesh.dto.UserDto;
import dev.jlkeesh.dto.UserUpdateDto;
import dev.jlkeesh.service.UserService;

import java.util.List;

public class RestUserService implements UserService {
    private final UserDao userDao;

    public RestUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserDto> getAll(UserCriteria criteria) {
        List<User> users = userDao.findAll();
    }

    @Override
    public UserDto get(Long aLong) {
        return null;
    }

    @Override
    public Long create(UserCreateDto dto) {
        return 0L;
    }

    @Override
    public boolean update(Long aLong, UserUpdateDto dto) {
        return false;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
