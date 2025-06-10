package com.example.assessment.etiqa.repository;

import com.example.assessment.etiqa.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
