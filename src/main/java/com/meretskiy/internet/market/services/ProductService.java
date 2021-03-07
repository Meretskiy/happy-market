package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.model.Product;
import com.meretskiy.internet.market.repositories.ProductRepository;
import com.meretskiy.internet.market.soap.productsWS.ProductWS;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final Function<Product, ProductWS> functionEntityToSoap = se -> {
        ProductWS productWS = new ProductWS();
        productWS.setId(se.getId());
        productWS.setTitle(se.getTitle());
        productWS.setPrice(se.getPrice());
        return productWS;
    };

    public Page<ProductDto> findAll(Specification<Product> spec, int page, int pageSize) {
        //ищем в базе страницу продуктов, с помощью фильтров спецификации
        Page<Product> originalPage = productRepository.findAll(spec, PageRequest.of(page - 1, pageSize));
        //перегоняем в страницу продуктов в Дто и возвращаем
        return originalPage.map(ProductDto::new);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductWS findProductWSById(long id) {
        return productRepository.findById(id).map(functionEntityToSoap).get();
    }

    public List<ProductWS> findAllProductsWS() {
        return productRepository.findAll().stream().map(functionEntityToSoap).collect(Collectors.toList());
    }

    public Optional<ProductDto> findProductDtoById(Long id) {
        return productRepository.findById(id).map(ProductDto::new);
    }

    public ProductDto saveProduct(ProductDto productDto) {
        Product newProduct = new Product();
        newProduct.setTitle(productDto.getTitle());
        newProduct.setPrice(productDto.getPrice());
        productRepository.save(newProduct);
        return productDto;
    }

    public ProductDto updateProduct(ProductDto productDto) {
        Long id = productDto.getId();
        Product product = productRepository.findById(id).get();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        productRepository.save(product);
        return productDto;
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }


}
