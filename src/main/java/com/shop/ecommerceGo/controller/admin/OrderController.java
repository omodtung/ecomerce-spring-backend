package com.shop.ecommerceGo.controller.admin;

import com.shop.ecommerceGo.domain.Order;
import com.shop.ecommerceGo.service.OrderService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/admin/order")
  public String getDashboard(
    Model model,
    @RequestParam("page") Optional<String> pageOptional
  ) {
    int page = 1;
    try {
      if (pageOptional.isPresent()) {
        page = Integer.parseInt(pageOptional.get());
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    Pageable pageable = PageRequest.of(page - 1, 1);
    Page<Order> ordersPage = this.orderService.fetchAllOrders(pageable);
    List<Order> orders = ordersPage.getContent();
    model.addAttribute("orders", orders);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", ordersPage.getTotalPages());
    return "admin/order/show";
  }
}
