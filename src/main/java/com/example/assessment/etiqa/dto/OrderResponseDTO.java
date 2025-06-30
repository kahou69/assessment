package com.example.assessment.etiqa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private List<OrderItemResponseDTO> orderItems;
    private BigDecimal totalPrice;
    private String status;

}