package com.may26.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.may26.entity.User;
import com.may26.service.UserService;
import com.may26.task.enums.Role;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // ✅ 1. Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ 2. Get user by ID
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ 3. Update role (PROMOTE / DEMOTE USER)
    @PutMapping("/user/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestParam Role role) {

        return ResponseEntity.ok(userService.updateRole(id, role));
    }

    // ✅ 4. Delete user
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
 
 // Get deleted users
    @GetMapping("/users/deleted")
    public List<User> getDeletedUsers() {
        return userService.getDeletedUsers();
    }
 // Restore deleted user
    @PutMapping("/user/{id}/restore")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {

        userService.restoreUser(id);

        return ResponseEntity.ok("User restored successfully");
    }
}