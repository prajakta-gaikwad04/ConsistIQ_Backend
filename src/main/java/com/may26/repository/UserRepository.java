package com.may26.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.may26.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}