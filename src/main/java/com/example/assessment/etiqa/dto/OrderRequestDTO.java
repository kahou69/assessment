package com.example.assessment.etiqa.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    List<OrderItemRequestDTO> orderItems;
}
