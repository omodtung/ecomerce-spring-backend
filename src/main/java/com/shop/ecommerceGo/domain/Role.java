package com.shop.ecommerceGo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private String description;

  // role - one => many - users . ctrl + k . press 's'
  //   bi user cam khoa nen se co mappedBy
  // User là bên sở hữu quan hệ.

  //   @OneToMany yêu cầu kiểu dữ liệu phải là một collection (như List, Set, hoặc Map) để chứa nhiều đối tượng User
  @OneToMany(mappedBy = "role")
  private List<User> users;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return (
      "Role [id=" + id + ", name=" + name + ", description=" + description + "]"
    );
  }
}
