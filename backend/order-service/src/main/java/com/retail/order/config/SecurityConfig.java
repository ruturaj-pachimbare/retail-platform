
package com.retail.order.config;

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

                        // Place orders.
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        // List all orders: ADMIN only.
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders")
                        .hasRole("ADMIN")

                        // Customer-specific orders: ownership check in service.
                        .requestMatchers(
                                HttpMethod.GET, "/api/v1/orders/customer/**")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        // Individual order details: ownership check in service.
                        .requestMatchers(
                                HttpMethod.GET, "/api/v1/orders/*")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        // Cancellation: ownership check in service.
                        .requestMatchers(
                                HttpMethod.POST, "/api/v1/orders/*/cancel")
                        .hasAnyRole("CUSTOMER", "ADMIN")

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
