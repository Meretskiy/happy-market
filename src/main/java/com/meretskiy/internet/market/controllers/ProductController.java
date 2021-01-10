package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.model.Product;
import com.meretskiy.internet.market.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findProductById(@PathVariable Long id) {
        return productService.findProductById(id).get();
    }

    @PostMapping("/del/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        product.setId(null);
        return productService.save(product);
    }
}
