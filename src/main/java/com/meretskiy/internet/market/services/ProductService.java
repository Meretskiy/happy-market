package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.model.Product;
import com.meretskiy.internet.market.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductDto> findAll(int page) {
        //ищем в базе страницу продуктов
        Page<Product> originalPage = productRepository.findAll(PageRequest.of(page - 1, 5));
        //перегоняем в Дто и возвращаем продуктДто
        return new PageImpl<>(
                originalPage.getContent().stream().map(ProductDto::new).collect(Collectors.toList()),
                originalPage.getPageable(),
                originalPage.getTotalElements()
        );
    }

//    public List<ProductDto> findAll() {
//        //получаем из репозитория лист продуктов, мапим его в продуктДто и собираем в лист и возвращаем лист продуктДто
//        return productRepository.findAll().stream().map(ProductDto::new).collect(Collectors.toList());
//    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }


}
