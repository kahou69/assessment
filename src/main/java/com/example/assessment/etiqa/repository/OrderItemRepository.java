package com.example.assessment.etiqa.repository;

import com.example.assessment.etiqa.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductId(Long productId);
}
