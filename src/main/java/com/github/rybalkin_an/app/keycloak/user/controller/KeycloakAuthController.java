package com.github.rybalkin_an.app.keycloak.user.controller;

import com.github.rybalkin_an.app.keycloak.user.service.KeycloakTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class KeycloakAuthController {

    @Autowired
    private KeycloakTokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam String username,
            @RequestParam String password) {

        String token = tokenService.getBearerToken(username, password);
        return ResponseEntity.ok(token);
    }
}

