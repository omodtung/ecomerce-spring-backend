package com.shop.ecommerceGo.repository;

import com.shop.ecommerceGo.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // viet kieu nay khong toi uu :<<<  trash
  User save(User user);

  List<User> findAll();
  void deleteById(long id);
  List<User> findOneByEmail(String email);
  User findById(long id);

  @Query("select count(u) from User u where u.email = ?1")
  int checkExistEmaill(String email);
}
