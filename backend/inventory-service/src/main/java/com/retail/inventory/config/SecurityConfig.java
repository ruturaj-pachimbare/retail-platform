
package com.retail.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/inventory/**")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        // Order processing needs these operations.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/inventory/*/reservations",
                                "/api/v1/inventory/*/release"
                        ).hasAnyRole("CUSTOMER", "ADMIN")

                        // Other inventory write operations.
                        .requestMatchers(
                                HttpMethod.POST, "/api/v1/inventory/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT, "/api/v1/inventory/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/v1/inventory/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE, "/api/v1/inventory/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
