package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.dto.OrderItemRequestDTO;
import com.example.assessment.etiqa.dto.OrderItemResponseDTO;
import com.example.assessment.etiqa.dto.OrderResponseDTO;
import com.example.assessment.etiqa.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testPlaceOrder() throws Exception {
        Long customerId = 1L;

        List<OrderItemResponseDTO> orderItemResponse = List.of(
                new OrderItemResponseDTO(1L, "Lord of The Rings", 10, new BigDecimal("49.9"))
        );

        List<OrderItemRequestDTO> orderItemRequest = List.of(
                new OrderItemRequestDTO(1L, 10)
        );

        OrderResponseDTO responseDTO = new OrderResponseDTO(1L,
                customerId,
                orderItemResponse,
                new BigDecimal("499.0"),
                "PENDING");

        when(orderService.placeOrder(customerId, orderItemRequest)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/orders")
                        .param("customerId", customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(499.0))
                .andExpect(jsonPath("$.orderItems[0].productId").value(1))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Lord of The Rings"))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(10))
                .andExpect(jsonPath("$.orderItems[0].price").value(49.9));
    }

    @Test
    public void testOrderByCustomerId() throws Exception {
        List<OrderResponseDTO> responseDTOs = List.of(
                new OrderResponseDTO(1L,
                1L,
                List.of(new OrderItemResponseDTO(1L, "Lord of The Rings", 10, new BigDecimal("49.9"))
                ),
                new BigDecimal("499.0"),
                "PENDING")
        );

        when(orderService.findOrderByCustomerId(1L)).thenReturn(responseDTOs);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].orderItems[0].productId").value(1))
                .andExpect(jsonPath("$.[0].orderItems[0].productName").value("Lord of The Rings"))
                .andExpect(jsonPath("$.[0].orderItems[0].quantity").value(10))
                .andExpect(jsonPath("$.[0].orderItems[0].price").value(49.9));

    }
}
