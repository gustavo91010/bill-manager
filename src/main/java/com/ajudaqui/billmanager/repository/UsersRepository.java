package com.ajudaqui.billmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajudaqui.billmanager.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByEmail(String email);
}
