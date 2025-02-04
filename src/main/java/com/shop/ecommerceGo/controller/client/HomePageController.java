package com.shop.ecommerceGo.controller.client;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.domain.dto.RegisterDTO;
import com.shop.ecommerceGo.service.ProductService;
import com.shop.ecommerceGo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomePageController {

  private final ProductService productService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  // private final PasswordEncoder passwordEncoder;

  public HomePageController(
    ProductService productService,
    UserService userService,
    PasswordEncoder passwordEncoder
    // PasswordEncoder passwordEncoder
  ) {
    this.productService = productService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    // this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/")
  public String getHomePage(Model model) {
    List<Product> products = this.productService.fetchProducts();
    model.addAttribute("products", products);
    return "client/homepage/show";
  }

  @GetMapping("/register")
  public String getRegisterPage(Model model) {
    model.addAttribute("registerUser", new RegisterDTO());
    return "client/auth/register";
  }

  @PostMapping("/register")
  public String handleRegister(
    @ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
    BindingResult bingBindingResult,
    Model model
  ) {
    // User user =  userService.checkExistEmail(registerDTO.getEmail()) ; if want have a object will do like it :>>

    if (bingBindingResult.hasErrors()) {
      return "client/auth/register";
    }

    if (userService.checkExistEmail(registerDTO.getEmail())) {
      System.err.println("Trung email ");
      bingBindingResult.rejectValue(
        "email",
        "error.registerDTO",
        "Email already exists"
      );
      return "client/auth/register";
    }

    User user = this.userService.registerDTOtoUser(registerDTO);
    // String hashPassword = this.passwordhãy để cho anh thêm một lần yêu emEncoder.encode(user.getPassword());
    String hashPassword = this.passwordEncoder.encode(user.getPassword());
    user.setPassword(hashPassword);
    user.setRole(userService.getRoleByName("USER"));

    //

    this.userService.handleSaveUser(user);
    System.err.println("test user" + user);
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String getLoginPage(Model model) {
    return "client/auth/login";
  }
}
