package com.jwt.auth.jwtauth.repository;

import com.jwt.auth.jwtauth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {

    Optional<UserInfo> findByName(String username);
}
