package com.github.rybalkin_an.app.keycloak.user.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
public class KeycloakResourceController extends BaseKeycloakController {
    @Secured("ROLE_MEMBER")
    @GetMapping("/member")
    public String getMemberResource() {
        return "This is the MEMBER resource page.";
    }

    @Secured("ROLE_GUEST")
    @GetMapping("/guest")
    public String getGuestResource() {
        return "This is the GUEST resource page.";
    }
}
