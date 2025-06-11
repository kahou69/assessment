package com.example.assessment.etiqa.repository;

import com.example.assessment.etiqa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
