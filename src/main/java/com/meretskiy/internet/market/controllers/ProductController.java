package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.exceptions_handling.ResourceNotFoundException;
import com.meretskiy.internet.market.repositories.specifications.ProductSpecifications;
import com.meretskiy.internet.market.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> findAllProducts(
            //все эти параметры собираем в MultiValueMap, что бы удобнее было прокидывать параметры в методы
            //MultiValueMap - мапа в которой под одним клчем может лежать несколько значений
//            @RequestParam(name = "min_price", required = false) Integer minPrice,
//            @RequestParam(name = "max_price", required = false) Integer maxPrice,
//            @RequestParam(name = "title", required = false) String title,
            @RequestParam MultiValueMap<String, String> params,
            @RequestParam(name = "p", defaultValue = "1") Integer page
            ) {
        if (page < 1) {
            page = 1;
        }
        //прокидываем спецификацию в матол findAll
        return productService.findAll(ProductSpecifications.build(params), page, 5);
    }

    @GetMapping("/{id}")
    public ProductDto findProductById(@PathVariable Long id) {
        return productService.findProductDtoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " doesn't exist"));
    }

//    //ResponseEntity - продвинутый ответ, в который можно закинуть тело запроса, его статус код и прочие хедеры
//    @GetMapping("/{id}")
//    public ResponseEntity<?> findProductById(@PathVariable Long id) {
//        Optional<ProductDto> productDto =  productService.findProductById(id);
//        if (!productDto.isPresent()) {
//            return new ResponseEntity<>(new MarketError(404, "Product with id " + id + " doesn't exist"), HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(productDto.get(), HttpStatus.OK);
//    }

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
