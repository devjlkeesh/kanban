package dev.jlkeesh.mapper.db.impl;

import dev.jlkeesh.domain.Otp;
import dev.jlkeesh.mapper.db.OtpRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresOtpRowMapper implements OtpRowMapper {
    @Override
    public Otp mapTo(ResultSet resultSet) throws SQLException {
        Otp otp = new Otp();
        otp.setId(resultSet.getLong("id"));
        otp.setUsed(resultSet.getBoolean("used"));
        otp.setValidTill(resultSet.getTimestamp("valid_till").toLocalDateTime());
        otp.setCode(resultSet.getString("code"));
        otp.setUserId(resultSet.getLong("user_id"));
        return otp;
    }
}
