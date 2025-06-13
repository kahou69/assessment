package com.example.assessment.etiqa.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`order`")  // Use backticks to escape the reserved keyword
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    private BigDecimal totalPrice;
    private String status;

    @Temporal(TemporalType.DATE)
    private Date orderDate;







}
