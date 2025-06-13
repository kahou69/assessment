package com.example.assessment.etiqa.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private List<OrderItemResponseDTO> orderItems;
    private BigDecimal totalPrice;
    private String status;

}