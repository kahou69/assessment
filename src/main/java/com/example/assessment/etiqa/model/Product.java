package com.example.assessment.etiqa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Schema(description = "Product entity")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Title of the book")
    private String bookTitle;

    @Schema(description = "price of the book")
    private BigDecimal bookPrice;

    @Schema(description = "Quantity of the book available")
    private Integer bookQuantity;
}
