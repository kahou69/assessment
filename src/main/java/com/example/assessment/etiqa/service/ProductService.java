package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.exception.NotFoundException;
import com.example.assessment.etiqa.model.Product;
import com.example.assessment.etiqa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

   private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException("Product not found with id : " + id));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProductById(id);
        BeanUtils.copyProperties(updatedProduct, product, "id");
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        getProductById(id);
         productRepository.deleteById(id);
    }
}
