package com.shop.ecommerceGo.controller;

import com.shop.ecommerceGo.domain.User;
import jakarta.websocket.server.PathParam;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

  @RequestMapping("/admin/user")
  public String getUserPage(Model model) {
    return "admin/user/create";
  }
}
