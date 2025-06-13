package com.example.assessment.etiqa.repository;

import com.example.assessment.etiqa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
