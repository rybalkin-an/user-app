package com.github.rybalkin_an.app.keycloak.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = serverUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(source -> mapAuthorities(source.getClaims()));
        return converter;
    }

    private List<GrantedAuthority> mapAuthorities(final Map<String, Object> attributes) {
        final Map<String, Object> realmAccess =
                ((Map<String, Object>) attributes.getOrDefault("realm_access", Collections.emptyMap()));
        final Collection<String> roles =
                ((Collection<String>) realmAccess.getOrDefault("roles", Collections.emptyList()));
        return roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Bean
    public SecurityFilterChain oauthFilterChain(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/resources/member").hasRole("MEMBER")
                        .requestMatchers("/resources/guest").hasRole("GUEST")
                        .requestMatchers("/api/keycloak/auth/openid").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/users/manage", "/users/createUser" , "/users/deleteUser").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }
}
