package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Cart;
import com.shop.ecommerceGo.domain.CartDetail;
import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.repository.CartDetailRepository;
import com.shop.ecommerceGo.repository.CartRepository;
import com.shop.ecommerceGo.repository.ProductRepository;
import com.shop.ecommerceGo.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final UserService userService;

  public ProductService(
    ProductRepository productRepository,
    CartRepository cartRepository,
    CartDetailRepository cartDetailRepository,
    UserService userService
  ) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
  }

  public Product createProduct(Product pr) {
    return this.productRepository.save(pr);
  }

  public Optional<Product> fetchProductById(long id) {
    return this.productRepository.findById(id);
  }

  public List<Product> fetchProducts() {
    return this.productRepository.findAll();
  }

  public void deleteProduct(long id) {
    this.productRepository.deleteById(id);
  }

  public void handleAddProductToCart(
    String email,
    long productId,
    HttpSession session
  ) {
    User user = this.userService.FindUserByEmail(email);
    if (user != null) {
      Cart cart = this.cartRepository.findByUser(user);
      if (cart == null) {
        // add product to cart
        Cart otherCart = new Cart();
        otherCart.setUser(user);
        otherCart.setSum(1);
        cart = this.cartRepository.save(otherCart);
      }
      Optional<Product> productOptional =
        this.productRepository.findById(productId);
      if (productOptional.isPresent()) {
        Product realProduct = productOptional.get();
        CartDetail oldDetail =
          this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
        if (oldDetail == null) {
          CartDetail cd = new CartDetail();
          cd.setCart(cart);
          cd.setProduct(realProduct);
          cd.setPrice(realProduct.getPrice());
          cd.setQuantity(1);
          this.cartDetailRepository.save(cd);
          // update cart (sum);
          int s = cart.getSum() + 1;
          cart.setSum(s);
          this.cartRepository.save(cart);
          session.setAttribute("sum", s);
        } else {
          oldDetail.setQuantity(oldDetail.getQuantity() + 1);
          this.cartDetailRepository.save(oldDetail);
        }
      }
    }
  }

  public Cart fetchByUser(User user) {
    return this.cartRepository.findByUser(user);
  }
}
