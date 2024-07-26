package com.meetingApi.meetingRestApiDemo.dataacess;

import com.meetingApi.meetingRestApiDemo.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

 public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    @Transactional
    @Modifying
    @Query("update User  u set u.password= ?2 where  u.email = ?1")
    void updatePassword(String email,String password);
}