package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.exception.NotFoundException;
import com.example.assessment.etiqa.model.OrderItem;
import com.example.assessment.etiqa.model.Product;
import com.example.assessment.etiqa.repository.OrderItemRepository;
import com.example.assessment.etiqa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

   private final ProductRepository productRepository;
   private final OrderItemRepository orderItemRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new NotFoundException("Product not found with id : " + id);
                });
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProductById(id);
        BeanUtils.copyProperties(updatedProduct, product, "id");
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(id);
        if (!orderItems.isEmpty()) {
            log.error("Cannot Delete Product with ID: {}", id);
            throw new IllegalArgumentException("Cannot delete product. It is associated with existing orders.");
        }
        getProductById(id);
         productRepository.deleteById(id);
    }

    public Product restockProduct(Long id , Integer quantity) {

        //check restock quantity is bigger than 0 or not
        if (quantity <= 0) {
            log.error("Restock quantity should be greater than 0");
            throw new IllegalArgumentException("Restock quantity should be greater than 0");
        }

        Product product = getProductById(id);
        product.setBookQuantity(product.getBookQuantity() + quantity);
        return productRepository.save(product);
    }
}
