package com.techzone.auth.config;
import com.techzone.auth.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DemoUserSeeder {
 @Bean CommandLineRunner seed(AppUserRepository users,PasswordEncoder encoder){return args->{
  if(!users.existsByUsername("demo"))users.save(new AppUser("demo","demo@techzone.local",encoder.encode("Demo@12345"),"CUSTOMER"));
  if(!users.existsByUsername("admin"))users.save(new AppUser("admin","admin@techzone.local",encoder.encode("Admin@12345"),"ADMIN"));
 };}
}
