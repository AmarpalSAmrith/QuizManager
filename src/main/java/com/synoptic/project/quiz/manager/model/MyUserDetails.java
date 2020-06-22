package com.synoptic.project.quiz.manager.model;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class MyUserDetails implements UserDetails {

  private String username;
  private String password;
  private List<GrantedAuthority> authorities;
  private boolean enabled;

  public MyUserDetails(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.authorities = user.getAuthorities().stream()
        .map(Authority::getAuthority)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    this.enabled = user.getActive();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

}
