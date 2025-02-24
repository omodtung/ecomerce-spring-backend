package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.domain.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Product save(Product pr);
  void deleteById(long id);

  List<Product> findAll();
  Optional<Product> findById(long id);
}
