package com.github.rybalkin_an.app.keycloak.user.controller;

import com.github.rybalkin_an.app.keycloak.user.service.KeycloakTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keycloak/auth")
public class KeycloakAuthController {

    @Autowired
    private KeycloakTokenService tokenService;

    @PostMapping("/openid")
    public ResponseEntity<String> getToken(
            @RequestParam String username,
            @RequestParam String password) {

        String token = tokenService.getBearerToken(username, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/debug/authorities")
    public ResponseEntity<String> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No authentication found.");
        }

        if (authentication.getAuthorities().isEmpty()) {
            return ResponseEntity.ok("Authentication found, but no authorities assigned.");
        }

        StringBuilder authorities = new StringBuilder();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.append(authority.getAuthority()).append("\n");
        }

        return ResponseEntity.ok("Authorities:\n" + authorities);
    }

}

