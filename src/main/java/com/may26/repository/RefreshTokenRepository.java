package com.may26.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.may26.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByEmail(String email);
}