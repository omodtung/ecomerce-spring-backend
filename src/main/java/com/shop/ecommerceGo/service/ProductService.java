package com.shop.ecommerceGo.service;

import com.shop.ecommerceGo.domain.Product;
import com.shop.ecommerceGo.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
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
}
