package com.example.assessment.etiqa.controller;

import com.example.assessment.etiqa.model.Product;
import com.example.assessment.etiqa.service.CacheInspectionService;
import com.example.assessment.etiqa.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final CacheInspectionService cacheInspectionService;

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Get a product by id")
    @Cacheable(value = "products", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "create a new product entry")
    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @Operation(summary = "update a product by id")
    @CachePut(value = "products", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @Operation(summary = "delete a product by id")
    @CacheEvict(value = "products", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restock quantity by id")
    @CachePut(value = "products", key = "#id")
    @PutMapping("/{id}/restock")
    public ResponseEntity<Product> restockProduct(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(productService.restockProduct(id, quantity));
    }

    @GetMapping("/caches")
    public void getCacheData() {
        cacheInspectionService.printCacheContents("products");
    }

}
