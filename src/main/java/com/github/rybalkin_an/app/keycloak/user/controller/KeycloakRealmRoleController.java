package com.github.rybalkin_an.app.keycloak.user.controller;

import com.github.rybalkin_an.app.keycloak.user.service.KeycloakRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keycloak/realm-roles")
public class KeycloakRealmRoleController {

    @Autowired
    private KeycloakRoleService roleService;

    @GetMapping()
    @Operation(summary = "Get a role by name", description = "Retrieves a specific role by its name.")
    public ResponseEntity<RoleRepresentation> getRoleByName(@RequestParam String roleName) {
        RoleRepresentation role = roleService.getRoleByName(roleName);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    @Operation(summary = "Create a new role", description = "Creates a new role in the Keycloak realm.")
    public ResponseEntity<String> createRole(@RequestParam String roleName) {
        roleService.createRole(roleName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Role '" + roleName + "' created successfully.");
    }

    @DeleteMapping
    @Operation(summary = "Delete a role", description = "Deletes a role from the Keycloak realm.")
    public ResponseEntity<String> deleteRole(@RequestParam String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Role '" + roleName + "' deleted successfully.");
    }

}
