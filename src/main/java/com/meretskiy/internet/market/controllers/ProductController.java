package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> findAllProducts(
            @RequestParam(name = "min_price", defaultValue = "0") Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "p", defaultValue = "1") Integer page
    ) {
        if (page < 1) {
            page = 1;
        }
        return productService.findAll(page);
    }

    @GetMapping("/{id}")
    public ProductDto findProductById(@PathVariable Long id) {
        return productService.findProductById(id).get();
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PostMapping
    //если запрос выполнится, то он вернет 201
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto saveNewProduct(@RequestBody ProductDto productDto) {
        productDto.setId(null);
        return productService.saveProduct(productDto);
    }

    @PutMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }
}
