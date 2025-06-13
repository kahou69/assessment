package com.example.assessment.etiqa.repository;

import com.example.assessment.etiqa.dto.OrderResponseDTO;
import com.example.assessment.etiqa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByCustomerId(Long customerId);
}
