package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.model.Product;
import com.example.assessment.etiqa.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void TestGetAllProducts() throws Exception {
        List<Product> mockList = List.of(
                new Product(1L, "World Of Warcraft", new BigDecimal("49.90"), 10),
                new Product(2L, "Prisoner of Azkaban", new BigDecimal("49.9"), 5),
                new Product(3L, "Lord of The Rings", new BigDecimal("49.9"), 3)
        );

        when(productService.getAllProducts()).thenReturn(mockList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].bookTitle").value("Lord of The Rings"));
    }

    @Test
    void testGetProductById() throws Exception {
        Product mockProduct = new Product(1L, "Lord of The Rings", new BigDecimal("49.9"), 8);

        when(productService.getProductById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookQuantity").value(8));
    }

    @Test
    void testSaveProduct() throws Exception {
        Product product = new Product(null, "Domain-Driven Design", new BigDecimal("59.99"), 5);
        Product savedProduct = new Product(2L, "Domain-Driven Design", new BigDecimal("59.99"), 5);

        when(productService.saveProduct(any())).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product update = new Product(null, "Refactoring", new BigDecimal("39.99"), 8);
        Product updated = new Product(3L, "Refactoring", new BigDecimal("39.99"), 8);

        when(productService.updateProduct(eq(3L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/products/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("Refactoring"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/3"))
                .andExpect(status().isNoContent());
    }
}
