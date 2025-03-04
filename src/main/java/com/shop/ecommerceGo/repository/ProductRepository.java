package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.domain.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Product save(Product pr);
  void deleteById(long id);

  List<Product> findAll();
  Optional<Product> findById(long id);

    Page<Product> findAll(Pageable page);

    Page<Product> findAll(Specification<Product> spec, Pageable page);
}
