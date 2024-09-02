package com.github.rybalkin_an.app.keycloak.user.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/resources")
public class KeycloakResourceController {
    @Secured("MEMBER")
    @GetMapping("/member")
    public String getMemberResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("User Roles: ");
        for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
        }

        return "Member resource accessed";    }

    @Secured("GUEST")
    @GetMapping("/guest")
    public String getGuestResource() {
        return "This is the GUEST resource";
    }
}
