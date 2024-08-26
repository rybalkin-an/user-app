package com.github.rybalkin_an.app.keycloak.user.service;

import com.github.rybalkin_an.app.keycloak.user.model.UserRole;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class KeycloakRoleService {

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;

    public void createRole(String roleName) {
        try {
            if (keycloak.realm(realm).roles().get(roleName).toRepresentation() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Role with name " + roleName + " already exists");
            }
        } catch (Exception e) {
            // Role doesn't exist, proceed to create it
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            keycloak.realm(realm).roles().create(role);
        }
    }

    public void deleteRole(String roleName) {
        RoleResource roleResource;
        try {
            roleResource = keycloak.realm(realm).roles().get(roleName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role with name " + roleName + " not found");
        }
        roleResource.remove();
    }

    public RoleRepresentation getRoleByName(String roleName) {
        try {
            return keycloak.realm(realm).roles().get(roleName).toRepresentation();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role with name " + roleName + " not found");
        }
    }

    public List<RoleRepresentation> getUserRoles(String userId) {
        try {
            return keycloak.realm(realm).users().get(userId).roles().realmLevel().listEffective();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve roles for user with ID " + userId);
        }
    }

    public void assignRoleToUser(String userId, String roleName) {
        try {
            if (!UserRole.isValidRole(roleName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Only 'MEMBER' and 'GUEST' roles can be assigned.");
            }
            RoleRepresentation role = getRoleByName(roleName);
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(role));
        } catch (ResponseStatusException e) {
            // Re-throw ResponseStatusException for handling specific scenarios
            throw e;
        } catch (Exception e) {
            // Handle other exceptions with a generic error message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to assign role to user");
        }
    }

    public void removeRoleFromUser(String userId, String roleName) {
        try {
            RoleRepresentation role = getRoleByName(roleName);
            keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(List.of(role));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to remove role from user");
        }
    }
}
