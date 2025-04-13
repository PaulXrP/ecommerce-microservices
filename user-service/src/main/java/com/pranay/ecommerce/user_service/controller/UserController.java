package com.pranay.ecommerce.user_service.controller;

import com.pranay.ecommerce.user_service.dto.UserRequest;
import com.pranay.ecommerce.user_service.dto.UserResponse;
import com.pranay.ecommerce.user_service.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to add user: {}", userRequest.getFirstName());
        UserResponse addedUser = userService.addUser(userRequest);
        log.info("Successfully added user with ID: {}", addedUser.getId());
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @GetMapping("/find/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long userId) {
        log.info("Finding user with ID: {}", userId);
        UserResponse user = userService.findUser(userId);
        log.info("Found user : {}", user.getFirstName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/All")
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> allUsers = userService.fetchAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequest userRequest) {
        String updatedUser = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String deleted = userService.deleteUser(userId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
