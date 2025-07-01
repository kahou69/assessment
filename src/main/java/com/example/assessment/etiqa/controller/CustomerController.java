package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.dto.CustomerDTO;
import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.service.CacheInspectionService;
import com.example.assessment.etiqa.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final CacheInspectionService cacheInspectionService;


    @Operation(summary = "Get all Customers")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Get customer by id")
    @Cacheable(value = "customers", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "create a new customer")
    @PostMapping
    public ResponseEntity<CustomerDTO> saveCustomer (@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.saveCustomer(customerDTO));
    }

    @Operation(summary = "Update a single customer by id")
    @CachePut(value = "customers", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }


    @Operation(summary = "Delete a single customer by id")
    @CacheEvict(value = "customers", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/caches")
    public void getCacheData() {
        cacheInspectionService.printCacheContents("customers");
    }
}
