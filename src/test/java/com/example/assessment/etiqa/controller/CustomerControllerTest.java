package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.model.EmailType;
import com.example.assessment.etiqa.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCustomers() throws Exception {
        List<Customer> mockList = List.of(
                new Customer(1L, "Alice", "Smith",
                        Map.of(EmailType.OFFICE, "alice@company.com", EmailType.PERSONAL, "alice@gmail.com"),
                        List.of("bob@domain.com", "boba@gmail.com"), null)
        );

        when(customerService.getAllCustomers()).thenReturn(mockList);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    void testGetCustomerById() throws Exception {
     Customer mockCustomer = new Customer(1L, "Alice", "Smith",
                        Map.of(EmailType.OFFICE, "alice@company.com", EmailType.PERSONAL, "alice@gmail.com"),
                        List.of("bob@domain.com", "boba@gmail.com"), null);

        when(customerService.getCustomerById(2L)).thenReturn(mockCustomer);

        mockMvc.perform(get("/api/customers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testSaveCustomer() throws Exception {
        Customer customer = new Customer(null, "John", "Doe",
                Map.of(EmailType.OFFICE, "john@office.com"),
                List.of("family@domain.com"), null);

        Customer savedCustomer = new Customer(5L, "John", "Doe",
                customer.getEmails(),
                customer.getFamilyMembers(), null);

        when(customerService.saveCustomer(any())).thenReturn(customerService.mapToCustomerDTO(savedCustomer));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer update = new Customer(null, "Updated", "User",
                Map.of(EmailType.OFFICE, "new@office.com"),
                List.of("updated@domain.com"), null);

        Customer updated = new Customer(1L, "Updated", "User",
                update.getEmails(), update.getFamilyMembers(), null);

        when(customerService.updateCustomer(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }



}
