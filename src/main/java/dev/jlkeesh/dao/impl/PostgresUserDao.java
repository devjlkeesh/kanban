package dev.jlkeesh.dao.impl;

import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.domain.User;
import dev.jlkeesh.exception.DataAccessException;
import dev.jlkeesh.mapper.db.UserRowMapper;
import dev.jlkeesh.utils.PasswordUtil;
import dev.jlkeesh.utils.UserSession;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
public class PostgresUserDao implements UserDao {
    private static final String INSERT_QUERY = """
            insert into users (username, password, email, created_by, role)
            values (?, ?, ?, ?, ?)  returning *;""";
    private static final String SELECT_QUERY = "select t.* from users t where t.is_deleted = false ";
    private static final String UPDATE_QUERY = """
            update users
            set username   = ?,
                email      = ?,
                role       = ?,
                updated_by = ?,
                updated_at = current_timestamp
            where id = ?
            returning *;
            """;

    private static final String RESET_PASSWORD = """
            update users
            set password   = ?
            where id = ?
            """;

    private static final String SOFT_DELETE_QUERY = """
            update users
            set is_deleted = true,
                updated_at = current_timestamp,
                updated_by = ?
            where id = ?
            returning *;""";


    private final Connection connection;
    private final UserRowMapper userRowMapper;

    public PostgresUserDao(Connection connection, UserRowMapper userRowMapper) {
        this.connection = connection;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<User> findByUsername(@NonNull String username) {
        try (PreparedStatement pstm = connection.prepareStatement(SELECT_QUERY + " and t.username ilike ?")) {
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User user = userRowMapper.mapTo(rs);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.severe("error while getting user by username: " + username);
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(@NonNull String email) {
        try (PreparedStatement pstm = connection.prepareStatement(SELECT_QUERY + "and t.email ilike ?")) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User user = userRowMapper.mapTo(rs);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.severe("error while getting user by email: " + email);
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<User> findByEmailOrUsername(@NonNull String subject) {
        try (PreparedStatement pstm = connection.prepareStatement(SELECT_QUERY + " and (t.email ilike ? or t.username ilike ?)")) {
            pstm.setString(1, subject);
            pstm.setString(2, subject);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                User user = userRowMapper.mapTo(rs);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.severe("error while getting user : " + subject);
            throw new DataAccessException(e);
        }
    }


    @Override
    public User save(User user) {
        try (PreparedStatement pstm = connection.prepareStatement(INSERT_QUERY)) {
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getPassword());
            pstm.setString(3, user.getEmail());
            pstm.setLong(4, UserSession.requireUserId());
            pstm.setString(5, user.getRole().toString());
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                user.setCreatedAt(createdAt.toLocalDateTime());
                return user;
            }
            throw new SQLException("error on insert");
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user", e);
        }
    }

    @Override
    public User update(User user) {
        try (PreparedStatement psmt = connection.prepareStatement(UPDATE_QUERY)) {
            psmt.setString(1, user.getUsername());
            psmt.setString(2, user.getEmail());
            psmt.setString(3, user.getRole().toString());
            psmt.setLong(4, UserSession.requireUserId());
            psmt.setLong(5, user.getId());
            ResultSet resultSet = psmt.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                user.setUpdatedAt(updated_at.toLocalDateTime());
                return user;
            }
            throw new SQLException("Error on update user");
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user", e);
        }
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        try (PreparedStatement psmt = connection.prepareStatement(RESET_PASSWORD)) {
            psmt.setString(1, PasswordUtil.hash(newPassword));
            psmt.setLong(2, userId);
            psmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user", e);
        }
    }

    @Override
    public void delete(User user) {
        deleteById(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement psmt = connection.prepareStatement(SOFT_DELETE_QUERY)) {
            psmt.setLong(1, UserSession.requireUserId());
            psmt.setLong(2, id);
            psmt.execute();
            if (!psmt.execute()) {
                throw new SQLException("No user found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (PreparedStatement psmt = connection.prepareStatement(SELECT_QUERY + " and t.id = ?")) {
            psmt.setLong(1, id);
            ResultSet resultSet = psmt.executeQuery();
            if (resultSet.next()) {
                User user = userRowMapper.mapTo(resultSet);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by id", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (PreparedStatement psmt = connection.prepareStatement(SELECT_QUERY);
             ResultSet resultSet = psmt.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(userRowMapper.mapTo(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DataAccessException("Error finding users", e);
        }
    }
}
