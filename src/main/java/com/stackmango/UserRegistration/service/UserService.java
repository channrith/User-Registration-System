package com.stackmango.UserRegistration.service;

import com.stackmango.UserRegistration.entity.User;
import com.stackmango.UserRegistration.repository.UserRepository;

import java.util.List;
import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  // @Autowired
  // UserRepository userRepository;

  private final UserRepository userRepository;

  // Constructor Injection
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Get all users
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Get user by ID
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  // Update an existing user
  public boolean updateUser(Long id, User updatedUser) {
    Optional<User> existingUserOptional = userRepository.findById(id);
    if (existingUserOptional.isPresent()) {
      User existingUser = existingUserOptional.get();
      existingUser.setUserName(updatedUser.getUserName());
      existingUser.setEmail(updatedUser.getEmail());
      existingUser.setPassword(updatedUser.getPassword());
      userRepository.save(existingUser);
      return true;
    }
    return false;
  }

  // Delete user by ID
  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true; // Deletion was successful
    }
    return false; // User not found
  }
}
