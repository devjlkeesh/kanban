package dev.jlkeesh.dao;

import dev.jlkeesh.domain.Otp;

import java.util.Optional;

public interface OtpDao extends BaseDao<Otp, Long> {
    Optional<Otp> findByCodeAndNotUsed(String code);
}
