package com.techzone.auth.user;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
@RestController @RequestMapping("/api/v1/auth")
public class RegistrationController {
 public record Request(@NotBlank @Size(min=3,max=120) String username,@NotBlank @Email String email,@NotBlank @Size(min=8,max=72) String password){}
 private final AppUserRepository users; private final PasswordEncoder encoder;
 public RegistrationController(AppUserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
 public Map<String,Object> register(@Valid @RequestBody Request r){
  if(users.existsByUsername(r.username()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Username already exists");
  if(users.existsByEmail(r.email()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
  AppUser u=users.save(new AppUser(r.username(),r.email(),encoder.encode(r.password()),"CUSTOMER"));
  return Map.of("id",u.getId(),"username",u.getUsername(),"email",u.getEmail(),"role",u.getRole());
 }
}
