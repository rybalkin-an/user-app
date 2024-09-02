package com.github.rybalkin_an.app.keycloak.user.controller;

import com.github.rybalkin_an.app.keycloak.user.dto.UserCreateDTO;
import com.github.rybalkin_an.app.keycloak.user.service.KeycloakRoleService;
import com.github.rybalkin_an.app.keycloak.user.service.KeycloakUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keycloak/users")
@Tag(name = "Keycloak User Controller", description = "APIs for user management using Keycloak")
public class KeycloakUserController {

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private KeycloakRoleService roleService;

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserRepresentation.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserRepresentation> searchByUserId(@PathVariable String id) {
        var user = keycloakUserService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Updates user details by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserRepresentation user) {
        keycloakUserService.updateUser(id, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user", description = "Deletes a user by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        keycloakUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create user", description = "Creates a new user with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        String userId = keycloakUserService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with ID: " + userId);
    }

    @Operation(summary = "Get roles assigned to a user", description = "Retrieves all roles assigned to a user.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoleRepresentation.class)))
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleRepresentation>> getUserRoles(@PathVariable String id) {
        var roles = roleService.getUserRoles(id);
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Assign a role to a user", description = "Assigns a specific role to a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    @PostMapping("/{id}/roles")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String id, @RequestParam String roleName) {
        roleService.assignRoleToUser(id, roleName);
        return ResponseEntity.ok("Role '" + roleName + "' assigned to user with ID '" + id + "'.");
    }

    @Operation(summary = "Remove a role from a user", description = "Removes a specific role from a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<String> removeRoleFromUser(@PathVariable String id, @RequestParam String roleName) {
        roleService.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok("Role '" + roleName + "' removed from user with ID '" + id + "'.");
    }
}
