package dev.jlkeesh.dao.impl;

import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.enums.AuthRole;
import dev.jlkeesh.properties.DatabaseProperties;
import dev.jlkeesh.utils.PasswordUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


class UserDaoImplTest {
    UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection(
                DatabaseProperties.url,
                DatabaseProperties.username,
                DatabaseProperties.password
        );
        userDao = new PostgresUserDao(connection);
    }

    @Test
    void findByUsername() {
        Optional<User> userOptional = userDao.findByUsername("AdMin");
        Assertions.assertTrue(userOptional.isPresent());
    }

    @Test
    void findByEmail() {
        Optional<User> userOptional = userDao.findByEmail("admin@mail.ru");
        Assertions.assertTrue(userOptional.isPresent());
    }

    @Test
    void insert() {
        User user = new User();
        user.setUsername("sherzod");
        user.setPassword(PasswordUtil.hash("123"));
        user.setEmail("sherzod@mail.ru");
        user.setRole(AuthRole.MANAGER);
        userDao.insert(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteById() {
    }

    @org.junit.jupiter.api.Test
    void findById() {
    }

    @org.junit.jupiter.api.Test
    void findAll() {
    }
}