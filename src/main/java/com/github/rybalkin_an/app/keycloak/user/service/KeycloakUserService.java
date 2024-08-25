package com.github.rybalkin_an.app.keycloak.user.service;

import com.github.rybalkin_an.app.keycloak.user.dto.UserCreateDTO;
import com.github.rybalkin_an.app.user.exception.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUserService {

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;

    public UserRepresentation getUserById(String id) {
        try {
            return keycloak.realm(realm).users().get(id).toRepresentation();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    public RealmRepresentation getRealms() {
        try {
            return keycloak.realm(realm).toRepresentation();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    public void updateUser(String id, UserRepresentation updatedUser) {
        try {
            var existingUser = getUserById(id);

            if (isDuplicateUser(updatedUser, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this username or email already exists");
            }

            keycloak.realm(realm).users().get(id).update(updatedUser);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found");
        }
    }

    public void deleteUser(String id) {
        keycloak.realm(realm).users().get(id).remove();
    }

    public String createUser(UserCreateDTO userCreateDTO) {
        var user = getUserRepresentation(userCreateDTO);

        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() == HttpStatus.CREATED.value()) {
                String location = response.getHeaderString("Location");
                return location.substring(location.lastIndexOf('/') + 1);
            } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user");
            }
        }
    }

    private UserRepresentation getUserRepresentation(UserCreateDTO userCreateDTO) {
        var user = new UserRepresentation();
        user.setUsername(userCreateDTO.getUsername());
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setEmail(userCreateDTO.getEmail());
        user.setEnabled(true);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(userCreateDTO.getPassword());
        credentials.setTemporary(false);
        user.setCredentials(Collections.singletonList(credentials));
        return user;
    }

    private boolean isDuplicateUser(UserRepresentation updatedUser, String userId) {
        List<UserRepresentation> usersWithSameUsername = keycloak.realm(realm)
                .users()
                .search(updatedUser.getUsername(), true);

        List<UserRepresentation> usersWithSameEmail = keycloak.realm(realm)
                .users()
                .search(null, null, null, updatedUser.getEmail(), null, null);

        return usersWithSameUsername.stream().anyMatch(user -> !user.getId().equals(userId)) ||
                usersWithSameEmail.stream().anyMatch(user -> !user.getId().equals(userId));
    }
}