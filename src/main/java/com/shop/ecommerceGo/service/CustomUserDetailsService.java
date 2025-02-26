package com.shop.ecommerceGo.service;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    com.shop.ecommerceGo.domain.User user =
      this.userService.FindUserByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("user not found");
    }

    return new User(
      user.getEmail(),
      user.getPassword(),
      Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
      )
    );
  }
}
