package com.github.rybalkin_an.app.user.controller;

import com.github.rybalkin_an.app.user.exception.NotFoundException;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@Tag(name = "Users Controller", description = "RESTful API for managing users.")
public record UserController(UserService userService) {

    @GetMapping
    @Operation(summary = "Get all users", description = "Get a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Get a specific user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> findById(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user and returns the created user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid user data provided")
    })
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        User createdUser = userService.create(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Invalid user data provided")
    })
    public ResponseEntity<User> update(@PathVariable @NotNull UUID id, @RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a user data", description = "Update an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> updateUserData(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(userService.saveUserDataToUser(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@PathVariable @NotNull UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
