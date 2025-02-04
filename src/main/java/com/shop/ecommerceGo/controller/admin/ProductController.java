package com.shop.ecommerceGo.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.service.ProductService;
import com.shop.ecommerceGo.service.UploadService;

import jakarta.validation.Valid;

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

  @GetMapping("/admin/product/create")
  public String getCreateProductPage(Model model) {
    model.addAttribute("newProduct", new Product());
    return "admin/product/create";
  }

  @PostMapping("/admin/product/create")
  public String handleCreateProduct(
    @ModelAttribute("newProduct") @Valid Product pr,
    BindingResult newProductBindingResult,
    @RequestParam("File") MultipartFile file
  ) {
    // validate
    if (newProductBindingResult.hasErrors()) {
      return "admin/product/create";
    }

    System.out.println(pr + "pr get name " + pr.getName());
    // upload image
    String image = this.uploadService.handleSaveUpLoadFile(file, "product");
    pr.setImage(image);

    this.productService.createProduct(pr);

    return "redirect:/admin/product";
  }

  @GetMapping("/admin/product/update/{id}")
  public String getUpdateProductPage(Model model, @PathVariable long id) {
    Optional<Product> currentProduct = this.productService.fetchProductById(id);

    model.addAttribute("newProduct", currentProduct.get());
    return "admin/product/update";
  }

  @PostMapping("/admin/product/update")
  public String handleUpdateProduct(
    @ModelAttribute("newProduct") @Valid Product pr,
    BindingResult newProductBindingResult,
    @RequestParam("File") MultipartFile file
  ) {
    if (newProductBindingResult.hasErrors()) {
      return "admin/product/update";
    }
    // Optional type
    Product currentProduct =
      this.productService.fetchProductById(pr.getId()).get();
    if (currentProduct != null) {
      if (!file.isEmpty()) {
        String img = this.uploadService.handleSaveUpLoadFile(file, "product");
        currentProduct.setImage(img);
      }
      currentProduct.setName(pr.getName());
      currentProduct.setPrice(pr.getPrice());
      currentProduct.setQuantity(pr.getQuantity());
      currentProduct.setDetailDesc(pr.getDetailDesc());
      currentProduct.setShortDesc(pr.getShortDesc());
      currentProduct.setFactory(pr.getFactory());
      currentProduct.setTarget(pr.getTarget());
      this.productService.createProduct(currentProduct);
    }
    return "admin/product/update";
  }


  @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product pr) {
        this.productService.deleteProduct(pr.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }
  
}
