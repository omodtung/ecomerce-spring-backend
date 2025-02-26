package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.Order;
import com.shop.ecommerceGo.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUser(User user);
  
}
