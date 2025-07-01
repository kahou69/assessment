package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.dto.CustomerDTO;
import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.model.EmailType;
import com.example.assessment.etiqa.service.CacheInspectionService;
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
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CacheInspectionService cis;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCustomers() throws Exception {
        // Create mock list of customers
        List<CustomerDTO> customerDTOList = List.of
                (new CustomerDTO(1L, "Alice", "Smith",
                Map.of(EmailType.OFFICE, "alice@company.com", EmailType.PERSONAL, "alice@gmail.com"),
                List.of("bob@domain.com", "boba@gmail.com")));

        when(customerService.getAllCustomers()).thenReturn(customerDTOList);

        // Perform the test
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(1L, "Alice", "Smith",
                Map.of(EmailType.OFFICE, "alice@company.com", EmailType.PERSONAL, "alice@gmail.com"),
                List.of("bob@domain.com", "boba@gmail.com"));

        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testSaveCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO(null, "John", "Doe",
                Map.of(EmailType.OFFICE, "john@office.com"),
                List.of("family@domain.com"));

        CustomerDTO savedCustomer = new CustomerDTO(5L, "John", "Doe",
                customer.getEmails(),
                customer.getFamilyMembers());

        when(customerService.saveCustomer(any())).thenReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO update = new CustomerDTO(null, "Updated", "User",
                Map.of(EmailType.OFFICE, "new@office.com"),
                List.of("updated@domain.com"));

        CustomerDTO updated = new CustomerDTO(1L, "Updated", "User",
                update.getEmails(), update.getFamilyMembers());

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
