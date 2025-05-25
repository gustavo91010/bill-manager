package com.ajudaqui.billmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.billmanager.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

  // @Query(value = "SELECT * FROM public.users where access_token= '729e759e-cd03-4319-a417-b2cee4463f0b';", nativeQuery = true)
  @Query(value = "SELECT * FROM users WHERE access_token= :accessToken", nativeQuery = true)
  Optional<Users> findByAccessToken(String accessToken);
}
