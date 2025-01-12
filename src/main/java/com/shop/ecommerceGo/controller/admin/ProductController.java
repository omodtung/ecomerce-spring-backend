package com.shop.ecommerceGo.controller.admin;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.service.ProductService;
import com.shop.ecommerceGo.service.UploadService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProductController {

  private final UploadService uploadService;
  private final ProductService productService;

  public ProductController(
    UploadService uploadService,
    ProductService productService
  ) {
    this.uploadService = uploadService;
    this.productService = productService;
  }

  @GetMapping("/admin/product")
  public String getProduct(Model model) {
    List<Product> prs = this.productService.fetchProducts();
    model.addAttribute("products", prs);
    return "admin/product/show";
  }
}
