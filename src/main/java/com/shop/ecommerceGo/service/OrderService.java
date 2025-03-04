package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Order;
import com.shop.ecommerceGo.domain.OrderDetail;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.domain.User;
import com.shop.ecommerceGo.repository.OrderDetailRepository;
import com.shop.ecommerceGo.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public OrderService(
    OrderRepository orderRepository,
    OrderDetailRepository orderDetailRepository
  ) {
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
  }

  public Page<Order> fetchAllOrders(Pageable page) {
    return this.orderRepository.findAll(page);
  }

  public Optional<Order> fetchOrderById(long id) {
    return this.orderRepository.findById(id);
  }

  public void deleteOrderById(long id) {
    Optional<Order> oderOptional = this.fetchOrderById(id);
    if (oderOptional.isPresent()) {
      Order order = oderOptional.get();
      List<OrderDetail> orderDetails = order.getOrderDetails();
      for (OrderDetail orderDetail : orderDetails) {
        this.orderDetailRepository.deleteById(orderDetail.getId());
      }
      this.orderRepository.deleteById(id);
    }
  }

  public void updateOrder(Order order) {
    Optional<Order> orderOptional = this.fetchOrderById(order.getId());
    if (orderOptional.isPresent()) {
      Order currentOrder = orderOptional.get();
      currentOrder.setStatus(order.getStatus());
      this.orderRepository.save(currentOrder);
    }
  }

  public List<Order> fetchOrderByUser(User user) {
    return this.orderRepository.findByUser(user);
  }
}
