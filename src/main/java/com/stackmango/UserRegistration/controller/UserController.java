package com.stackmango.UserRegistration.controller;

import com.stackmango.UserRegistration.entity.User;
import com.stackmango.UserRegistration.service.UserService;

import jakarta.validation.Valid;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

  // @Autowired
  // UserService userService;

  /*
   * Potential Issues with Field Injection
   * 1. Field injection relies on reflection, which is less efficient and harder
   * to debug
   * 2. Dependencies are injected after object construction, which can lead to
   * unexpected NullPointerExceptions if the object is used before injection is
   * complete
   */

  private final UserService userService;

  // No @Autowired needed for a single constructor
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "register";
    }
    userService.saveUser(user);
    return "redirect:/user/success";
  }

  @GetMapping("/{id}")
  public String getUserById(@PathVariable Long id, Model model) {
    return userService.getUserById(id)
        .map(user -> {
          model.addAttribute("user", user);
          return "user-details";
        })
        .orElseGet(() -> {
          model.addAttribute("errorMessage", "User not found");
          return "error";
        });

    // Optional<User> userOptional = userService.getUserById(id);
    // if (userOptional.isPresent()) {
    // model.addAttribute("user", userOptional.get());
    // return "user-details";
    // }
    // return "error";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    return userService.getUserById(id)
        .map(user -> {
          model.addAttribute("user", user);
          return "edit";
        })
        .orElseGet(() -> {
          model.addAttribute("errorMessage", "User not found");
          return "error";
        });

    // Optional<User> userOptional = userService.getUserById(id);
    // if (userOptional.isPresent()) {
    // model.addAttribute("user", userOptional.get());
    // return "edit";
    // }
    // return "error";
  }

  @PostMapping("/{id}/update")
  public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      return "edit";
    }
    if (userService.updateUser(id, user)) {
      return "redirect:/user/" + id;
    } else {
      model.addAttribute("errorMessage", "User not found");
      return "error";
    }
  }

  @GetMapping("/{id}/delete")
  public String deleteUser(@PathVariable Long id, Model model) {
    if (userService.deleteUser(id)) {
      return "redirect:/user/list";
    } else {
      model.addAttribute("errorMessage", "User not found");
      return "error";
    }
  }

  @GetMapping("/success")
  public String successPage() {
    return "success";
  }

  @GetMapping("/list")
  public String listUsers(Model model) {
    model.addAttribute("users", userService.getAllUsers());
    return "user-list";
  }
}
