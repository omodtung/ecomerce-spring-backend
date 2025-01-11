package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Role;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.repository.RoleRepository;
import com.shop.ecommerceGo.repository.UserRepository;
import org.eclipse.tags.shaded.org.apache.regexp.recompile;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public UserService(
    UserRepository userRepository,
    RoleRepository roleRepository
  ) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
  }

  public User handleSaveUser(User user) {
    User user_register = this.userRepository.save(user);
    return user_register;
  }

  public Role getRoleByName(String name) {
    return this.roleRepository.findByName(name);
  }
}
