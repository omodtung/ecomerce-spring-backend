package com.shop.ecommerceGo.controller.admin;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.service.UploadService;
import com.shop.ecommerceGo.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.net.PasswordAuthentication;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

  private final UserService userService;
  private final UploadService uploadService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(
    UserService userService,
    UploadService uploadService,
    ServletContext servletContext,
    PasswordEncoder passwordEncoder
  ) {
    this.userService = userService;
    this.uploadService = uploadService;
    this.passwordEncoder = passwordEncoder;
  }

  @RequestMapping("/admin/user")
  public String getUserPage(Model model) {
    List<User> users = this.userService.getAllUser();
    model.addAttribute("users1", users);
    return "admin/user/show";
  }

  @RequestMapping("/admin/user/{id}")
  public String getUserDetailPage(Model model, @PathVariable long id) {
    User user = this.userService.getUserById(id);
    model.addAttribute("user", user);
    model.addAttribute("id", id);
    return "admin/user/detail";
  }

  @GetMapping("/admin/user/create") // GET
  public String getCreateUserPage(Model model) {
    model.addAttribute("newUser", new User());
    return "admin/user/create";
  }

  @PostMapping(value = "/admin/user/create")
  public String createUserPage(
    Model model,
    @ModelAttribute("newUser") User user_new,
    @RequestParam("File") MultipartFile file
  ) {
    String avatar = this.uploadService.handleSaveUpLoadFile(file, "avatar");
    String hashPassword = this.passwordEncoder.encode(user_new.getPassword());
    user_new.setAvatar(avatar);
    user_new.setPassword(hashPassword);

    System.out.println("oke" + user_new.getRole().getName());
    // USER
    System.out.println("no oke" + user_new.getRole());
    // Role [id=0, name=USER, description=null]
    user_new.setRole(
      //it take an id of role to this
      // example Id = 10
      this.userService.getRoleByName(user_new.getRole().getName())
    );
    userService.handleSaveUser(user_new);
    return "redirect:/admin/user";
  }

  @RequestMapping("/admin/user/update/{id}") // GET
  public String getUpdateUserPage(Model model, @PathVariable long id) {
    User currentUser = this.userService.getUserById(id);
    model.addAttribute("newUser", currentUser);
    return "admin/user/update";
  }

  @PostMapping("/admin/user/update")
  public String postUpdateUser(
    Model model,
    @ModelAttribute("newUser") User update_user
  ) {
    User currentUser = this.userService.getUserById(update_user.getId());
    if (currentUser != null) {
      currentUser.setAddress(update_user.getAddress());
      currentUser.setFullName(update_user.getFullName());
      currentUser.setPhone(update_user.getPhone());

      // bug here
      this.userService.handleSaveUser(currentUser);
    }
    return "redirect:/admin/user";
  }

  @GetMapping("/admin/user/delete/{id}")
  public String getDeleteUserPage(Model model, @PathVariable long id) {
    model.addAttribute("id", id);
    // User user = new User();
    // user.setId(id);
    model.addAttribute("newUser", new User());
    return "admin/user/delete";
  }

  @PostMapping("/admin/user/delete")
  public String postDeleteUser(
    Model model,
    @ModelAttribute("newUser") User del_user
  ) {
    this.userService.deleteUser(del_user.getId());
    return "redirect:/admin/user";
  }
}
