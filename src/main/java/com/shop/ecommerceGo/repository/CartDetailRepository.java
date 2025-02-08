package com.shop.ecommerceGo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.shop.ecommerceGo.domain.*;
@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

}
