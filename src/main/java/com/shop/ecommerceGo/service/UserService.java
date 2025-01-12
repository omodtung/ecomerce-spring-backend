package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Role;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.repository.RoleRepository;
import com.shop.ecommerceGo.repository.UserRepository;
import java.util.List;
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
    System.out.println(
      "-getRoleByName-" + this.roleRepository.findByName(name)
    );
    // -getRoleByName-Role [id=2, name=USER, description=User thông thường]

    return this.roleRepository.findByName(name);
  }

  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  public void deleteUser(long id) {
    userRepository.deleteById(id);
  }

  public List<User> getUserByEmail(String email) {
    return this.userRepository.findOneByEmail(email);
  }

  public User getUserById(long id) {
    return userRepository.findById(id);
  }
}
