package com.github.rybalkin_an.app.keycloak.user.model;

public enum UserRole {
    MEMBER,
    GUEST;

    public static boolean isValidRole(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }
}
