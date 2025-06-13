package com.example.assessment.etiqa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


//this class is to record the price of the product at the time of order
@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;

    private Integer quantity;
    private BigDecimal price;
}
