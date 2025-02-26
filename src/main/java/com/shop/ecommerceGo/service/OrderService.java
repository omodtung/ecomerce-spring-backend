package com.shop.ecommerceGo.service;
import java.util.List;
import java.util.Optional;
import com.shop.ecommerceGo.domain.Order;
import com.shop.ecommerceGo.repository.OrderDetailRepository;
import com.shop.ecommerceGo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
