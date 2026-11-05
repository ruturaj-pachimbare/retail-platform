package com.techzone.auth.config;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.*;
import com.techzone.auth.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.*;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import java.security.*;
import java.util.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
@Configuration
public class SecurityConfig {
 @Value("${app.security.issuer}") String issuer;
 @Value("${app.security.postman-client-id}") String clientId;
 @Value("${app.security.postman-client-secret}") String clientSecret;
 @Bean @Order(1) SecurityFilterChain authorizationServer(HttpSecurity http)throws Exception{
  OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
  http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
  http.exceptionHandling(e->e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
  return http.build();
 }
 @Bean @Order(2) SecurityFilterChain appSecurity(HttpSecurity http)throws Exception{
  http.authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/register","/actuator/health").permitAll().anyRequest().authenticated()).formLogin(Customizer.withDefaults());
  return http.build();
 }
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean RegisteredClientRepository registeredClients(){
  RegisteredClient c=RegisteredClient.withId(UUID.randomUUID().toString()).clientId(clientId)
   .clientSecret(passwordEncoder().encode(clientSecret)).clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
   .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
   .redirectUri("https://oauth.pstmn.io/v1/callback").scope(OidcScopes.OPENID).scope(OidcScopes.PROFILE)
   .scope("retail.read").scope("retail.write")
   .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).requireProofKey(true).build())
   .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(java.time.Duration.ofMinutes(15)).refreshTokenTimeToLive(java.time.Duration.ofHours(8)).reuseRefreshTokens(false).build()).build();
  return new InMemoryRegisteredClientRepository(c);
 }
 @Bean JWKSource<com.nimbusds.jose.proc.SecurityContext> jwkSource(){
  try{KeyPairGenerator g=KeyPairGenerator.getInstance("RSA");g.initialize(2048);KeyPair kp=g.generateKeyPair();
   RSAKey key=new RSAKey.Builder((java.security.interfaces.RSAPublicKey)kp.getPublic()).privateKey(kp.getPrivate()).keyID(UUID.randomUUID().toString()).build();
   return new ImmutableJWKSet<>(new JWKSet(key));
  }catch(GeneralSecurityException e){throw new IllegalStateException(e);}
 }
 @Bean AuthorizationServerSettings authorizationServerSettings(){return AuthorizationServerSettings.builder().issuer(issuer).build();}
 @Bean OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(AppUserRepository users){
  return c->{if(c.getPrincipal()!=null)users.findByUsername(c.getPrincipal().getName()).ifPresent(u->c.getClaims().claim("roles",List.of("ROLE_"+u.getRole())).claim("email",u.getEmail()));};
 }
}
