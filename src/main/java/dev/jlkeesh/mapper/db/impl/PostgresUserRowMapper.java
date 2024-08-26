package dev.jlkeesh.mapper.db.impl;

import dev.jlkeesh.domain.User;
import dev.jlkeesh.enums.AuthRole;
import dev.jlkeesh.mapper.db.UserRowMapper;
import lombok.extern.java.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Log
public class PostgresUserRowMapper implements UserRowMapper {

    @Override
    public User mapTo(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setCreatedBy(resultSet.getLong("created_by"));

        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        user.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        Object updatedBy = resultSet.getObject("updated_by");
        user.setUpdatedBy(updatedBy != null ? (Long) updatedBy : null);

        user.setDeleted(resultSet.getBoolean("is_deleted"));
        user.setRole(AuthRole.valueOf(resultSet.getString("role")));
        return user;
    }
}
