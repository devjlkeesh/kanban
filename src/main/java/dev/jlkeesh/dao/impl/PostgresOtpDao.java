package dev.jlkeesh.dao.impl;

import dev.jlkeesh.dao.OtpDao;
import dev.jlkeesh.domain.Otp;
import dev.jlkeesh.exception.DataAccessException;
import dev.jlkeesh.mapper.db.OtpRowMapper;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Log
public class PostgresOtpDao implements OtpDao {
    private static final String INSERT_QUERY = """
            insert into otps(user_id, code) values(?,?) returning id, valid_till;
            """;
    private static final String SELECT_QUERY = """
            select t.* from otps t 
            """;

    private final Connection connection;
    private final OtpRowMapper otpRowMapper;

    public PostgresOtpDao(Connection connection, OtpRowMapper otpRowMapper) {
        this.connection = connection;
        this.otpRowMapper = otpRowMapper;
    }

    @Override
    public Optional<Otp> findByCodeAndNotUsed(String code) {
        try (PreparedStatement pstm = connection.prepareStatement(SELECT_QUERY + " where t.used = false and t.code=?")) {
            pstm.setString(1, code);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Otp otp = otpRowMapper.mapTo(rs);
                return Optional.of(otp);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.severe("error while getting otp by code: " + code);
            throw new DataAccessException(e);
        }
    }

    @Override
    public Otp save(Otp otp) {
        try (PreparedStatement pstm = connection.prepareStatement(INSERT_QUERY)) {
            pstm.setLong(1, otp.getUserId());
            pstm.setString(2, otp.getCode());
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                otp.setId(rs.getLong("id"));
                Timestamp createdAt = rs.getTimestamp("valid_till");
                otp.setValidTill(createdAt.toLocalDateTime());
                otp.setUsed(false);
                return otp;
            }
            throw new SQLException("error on insert");
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting otp", e);
        }
    }

    @Override
    public Otp update(Otp otp) {
        return null;
    }

    @Override
    public void delete(Otp otp) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public Optional<Otp> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Otp> findAll() {
        return List.of();
    }
}
