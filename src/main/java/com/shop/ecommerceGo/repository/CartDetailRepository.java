package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
  public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query("SELECT CASE WHEN COUNT(cd) > 0 THEN true ELSE false END FROM CartDetail cd WHERE cd.cart = :cart AND cd.product = :product")
  boolean existsByCartAndProduct(@Param("cart") Cart cart, @Param("product") Product product);

  @Query("SELECT cd FROM CartDetail cd WHERE cd.cart = :cart AND cd.product = :product")
  CartDetail findByCartAndProduct(@Param("cart") Cart cart, @Param("product") Product product);
}
