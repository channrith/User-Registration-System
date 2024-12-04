package com.stackmango.UserRegistration.repository;

import com.stackmango.UserRegistration.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
