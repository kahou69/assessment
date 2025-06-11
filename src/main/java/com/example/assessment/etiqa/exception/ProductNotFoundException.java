package com.example.assessment.etiqa.exception;

public class ProductNotFoundException extends RuntimeException{
   public ProductNotFoundException(String message) {
        super(message);
    }
}
