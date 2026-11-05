package com.techzone.auth.user;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="app_users")
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=120) private String username;
 @Column(nullable=false,unique=true,length=180) private String email;
 @Column(name="password_hash",nullable=false,length=255) private String passwordHash;
 @Column(nullable=false,length=30) private String role="CUSTOMER";
 @Column(nullable=false) private boolean enabled=true;
 @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
 protected AppUser(){}
 public AppUser(String u,String e,String p,String r){username=u;email=e;passwordHash=p;role=r;}
 public Long getId(){return id;} public String getUsername(){return username;} public String getEmail(){return email;}
 public String getPasswordHash(){return passwordHash;} public String getRole(){return role;} public boolean isEnabled(){return enabled;}
}
