package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  // test lai data de kiem tra
  Role findByName(String name);
}
