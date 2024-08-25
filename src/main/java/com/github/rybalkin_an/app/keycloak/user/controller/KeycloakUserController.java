package com.github.rybalkin_an.app.keycloak.user.controller;

import com.github.rybalkin_an.app.keycloak.user.dto.UserCreateDTO;
import com.github.rybalkin_an.app.keycloak.user.service.KeycloakUserService;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/keycloak/users")
public class KeycloakUserController {

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Operation(summary = "Get realm information")
    @GetMapping("/realm")
    public ResponseEntity<RealmRepresentation> searchRealm() {
        var realm = keycloakUserService.getRealms();
        return ResponseEntity.ok(realm);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserRepresentation.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserRepresentation> searchByUserId(
            @PathVariable String userId) {
        var user = keycloakUserService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Update user details by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String id,
            @RequestBody UserRepresentation user) {
        keycloakUserService.updateUser(id, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        keycloakUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create user", description = "Create a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        String userId = keycloakUserService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with ID: " + userId);
    }
}
