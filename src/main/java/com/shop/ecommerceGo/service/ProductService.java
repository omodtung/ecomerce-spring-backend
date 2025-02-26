package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Cart;
import com.shop.ecommerceGo.domain.CartDetail;
import com.shop.ecommerceGo.domain.Order;
import com.shop.ecommerceGo.domain.OrderDetail;
import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.repository.CartDetailRepository;
import com.shop.ecommerceGo.repository.CartRepository;
import com.shop.ecommerceGo.repository.OrderDetailRepository;
import com.shop.ecommerceGo.repository.OrderRepository;
import com.shop.ecommerceGo.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final UserService userService;
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public ProductService(
    ProductRepository productRepository,
    CartRepository cartRepository,
    CartDetailRepository cartDetailRepository,
    UserService userService,
    OrderRepository orderRepository,
    OrderDetailRepository orderDetailRepository
  ) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
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

  public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
    Optional<CartDetail> cardDetailOptional =
      this.cartDetailRepository.findById(cartDetailId);
    if (cardDetailOptional.isPresent()) {
      CartDetail cartDetail = cardDetailOptional.get();
      // list thong tin cua cart cha ( id  , sum , user ,cardDetail )
      Cart currentCart = cartDetail.getCart();
      System.err.println("lay toan bo " + currentCart);

      this.cartDetailRepository.deleteById(cartDetailId);
      if (currentCart.getSum() > 1) {
        int s = currentCart.getSum() - 1;
        currentCart.setSum(s);
        session.setAttribute("sum", s);
        this.cartRepository.save(currentCart);
      } else {
        this.cartRepository.deleteById(currentCart.getId());
        session.setAttribute("sum", 0);
      }
    }
  }

  public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
    for (CartDetail cartDetail : cartDetails) {
      // loop qua tung product trong cart

      Optional<CartDetail> cdOptional =
        this.cartDetailRepository.findById(cartDetail.getId());
      if (cdOptional.isPresent()) {
        CartDetail currentCartDetail = cdOptional.get();
        currentCartDetail.setQuantity(cartDetail.getQuantity());
        this.cartDetailRepository.save(currentCartDetail);
      }
    }
  }

  public void handlePlaceOrder(
    User user,
    HttpSession session,
    String receiverName,
    String receiverAddress,
    String receiverPhone
  ) {
    Cart cart = this.cartRepository.findByUser(user);
    if (cart != null) {
      List<CartDetail> cartDetails = cart.getCartDetails();

      Order order = new Order();
      order.setUser(user);
      order.setReceiverAddress(receiverAddress);
      order.setReceiverName(receiverName);
      order.setReceiverPhone(receiverPhone);
      order.setStatus("PENDING");
      double sum = 0;
      for (CartDetail cd : cartDetails) {
        sum += cd.getPrice();
      }
      order.setTotalPrice(sum);
      order = this.orderRepository.save(order);
      // create orderDetail
      for (CartDetail cd : cartDetails) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(cd.getProduct());
        orderDetail.setPrice(cd.getPrice());
        orderDetail.setQuantity(cd.getQuantity());
        this.orderDetailRepository.save(orderDetail);
      }
      // step 2: delete cart_detail and cart
      for (CartDetail cd : cartDetails) {
        this.cartDetailRepository.deleteById(cd.getId());
      }
      this.cartRepository.deleteById(cart.getId());
      session.setAttribute("sum", 0);
    }
  }
}
