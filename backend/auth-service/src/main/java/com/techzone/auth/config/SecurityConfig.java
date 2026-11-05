
package com.techzone.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import com.techzone.auth.user.AppUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;


//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;
import java.util.UUID;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

 @Value("${app.security.issuer}")
 String issuer;

 @Value("${app.security.postman-client-id}")
 String clientId;

 @Value("${app.security.postman-client-secret}")
 String clientSecret;

 @Bean
 @Order(1)
 SecurityFilterChain authorizationServer(HttpSecurity http) throws Exception {
  OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

  http
          .cors(Customizer.withDefaults())
          .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
          .oidc(Customizer.withDefaults());

  http.exceptionHandling(e ->
          e.authenticationEntryPoint(
                  new LoginUrlAuthenticationEntryPoint("/login")
          )
  );

  return http.build();
 }

 @Bean
 @Order(2)
 SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
  http
          .cors(Customizer.withDefaults())
          .authorizeHttpRequests(a -> a
                  .requestMatchers(
                          "/api/v1/auth/register",
                          "/actuator/health"
                  ).permitAll()
                  .anyRequest().authenticated()
          )
          .formLogin(Customizer.withDefaults());

  return http.build();
 }

 @Bean
 PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
 }

 @Bean
 RegisteredClientRepository registeredClients() {

  // Existing Postman confidential client
  RegisteredClient postmanClient = RegisteredClient
          .withId(UUID.randomUUID().toString())
          .clientId(clientId)
          .clientSecret(passwordEncoder().encode(clientSecret))
          .clientAuthenticationMethod(
                  ClientAuthenticationMethod.CLIENT_SECRET_BASIC
          )
          .authorizationGrantType(
                  AuthorizationGrantType.AUTHORIZATION_CODE
          )
          .authorizationGrantType(
                  AuthorizationGrantType.REFRESH_TOKEN
          )
          .redirectUri("https://oauth.pstmn.io/v1/callback")
          .scope(OidcScopes.OPENID)
          .scope(OidcScopes.PROFILE)
          .scope("retail.read")
          .scope("retail.write")
          .clientSettings(ClientSettings.builder()
                  .requireAuthorizationConsent(false)
                  .requireProofKey(true)
                  .build())
          .tokenSettings(TokenSettings.builder()
                  .accessTokenTimeToLive(
                          java.time.Duration.ofMinutes(15)
                  )
                  .refreshTokenTimeToLive(
                          java.time.Duration.ofHours(8)
                  )
                  .reuseRefreshTokens(false)
                  .build())
          .build();

  // New Angular public client using PKCE
  RegisteredClient angularClient = RegisteredClient
          .withId(UUID.randomUUID().toString())
          .clientId("retail-angular")
          .clientAuthenticationMethod(
                  ClientAuthenticationMethod.NONE
          )
          .authorizationGrantType(
                  AuthorizationGrantType.AUTHORIZATION_CODE
          )
          .redirectUri("http://localhost:4200/auth/callback")
          .postLogoutRedirectUri("http://localhost:4200/login")
          .scope(OidcScopes.OPENID)
          .scope(OidcScopes.PROFILE)
          .scope("retail.read")
          .scope("retail.write")
          .clientSettings(ClientSettings.builder()
                  .requireAuthorizationConsent(false)
                  .requireProofKey(true)
                  .build())
          .tokenSettings(TokenSettings.builder()
                  .accessTokenTimeToLive(
                          java.time.Duration.ofMinutes(15)
                  )
                  .build())
          .build();

  return new InMemoryRegisteredClientRepository(
          postmanClient,
          angularClient
  );
 }

 @Bean
 JWKSource<SecurityContext> jwkSource() {

  try {
   KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
   generator.initialize(2048);

   KeyPair keyPair = generator.generateKeyPair();

   RSAKey rsaKey = new RSAKey.Builder(
           (java.security.interfaces.RSAPublicKey) keyPair.getPublic()
   )
           .privateKey(keyPair.getPrivate())
           .keyID(UUID.randomUUID().toString())
           .build();

   JWKSet jwkSet = new JWKSet(rsaKey);

   return new ImmutableJWKSet<>(jwkSet);

  } catch (GeneralSecurityException exception) {
   throw new IllegalStateException(
           "Unable to generate RSA signing key",
           exception
   );
  }
 }

 @Bean
 AuthorizationServerSettings authorizationServerSettings() {

  return AuthorizationServerSettings.builder()
          .issuer(issuer)
          .build();
 }

 @Bean
 OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(
         AppUserRepository users
 ) {

  return context -> {

   if (context.getPrincipal() != null) {

    users.findByUsername(
            context.getPrincipal().getName()
    ).ifPresent(user -> {

     context.getClaims()
             .claim(
                     "roles",
                     List.of("ROLE_" + user.getRole())
             )
             .claim("email", user.getEmail());

    });
   }
  };
 }

 @Bean
 CorsConfigurationSource corsConfigurationSource() {

  CorsConfiguration configuration = new CorsConfiguration();

  configuration.setAllowedOrigins(List.of(
          "http://localhost:4200",
          "http://127.0.0.1:4200"
  ));

  configuration.setAllowedMethods(List.of(
          "GET",
          "POST",
          "PUT",
          "PATCH",
          "DELETE",
          "OPTIONS"
  ));

  configuration.setAllowedHeaders(List.of("*"));

  configuration.setAllowCredentials(true);

  configuration.setMaxAge(3600L);

  UrlBasedCorsConfigurationSource source =
          new UrlBasedCorsConfigurationSource();

  source.registerCorsConfiguration("/**", configuration);

  return source;
 }
}
