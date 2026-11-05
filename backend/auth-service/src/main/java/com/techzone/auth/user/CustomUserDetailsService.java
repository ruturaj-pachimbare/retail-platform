package com.techzone.auth.user;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {
 private final AppUserRepository repo;
 public CustomUserDetailsService(AppUserRepository repo){this.repo=repo;}
 public UserDetails loadUserByUsername(String username){
  AppUser u=repo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
  return new org.springframework.security.core.userdetails.User(u.getUsername(),u.getPasswordHash(),u.isEnabled(),true,true,true,
   List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole())));
 }
}
