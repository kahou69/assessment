package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.dto.OrderItemRequestDTO;
import com.example.assessment.etiqa.dto.OrderResponseDTO;
import com.example.assessment.etiqa.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "place an order with customer id as param, and orderItems")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestParam Long customerId, @RequestBody List<OrderItemRequestDTO> orderItems) {
        return ResponseEntity.ok(orderService.placeOrder(customerId, orderItems));
    }

    @Operation(summary = "Find order by customer Id")
    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> findOrderByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.findOrderByCustomerId(customerId));
    }
}
