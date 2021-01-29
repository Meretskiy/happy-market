package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.exceptions_handling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Cart {
    private Map<ProductDto, Integer> cartList;
    private final ProductService productService;

    @PostConstruct
    private void init() {
        cartList = new HashMap<>();
    }

    public Map<ProductDto, Integer> showCart() {
        return cartList;
    }

    public void addProductToCart(Long id) {
        ProductDto productDto = productService.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " doesn't exist"));
        if (!cartList.containsKey(productDto)) {
            cartList.put(productDto, 1);
        } else {
            cartList.put(productDto, cartList.get(productDto) + 1);
        }
    }

    public void clearProductFromCart(Long id) {
        ProductDto productDto = productService.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " doesn't exist"));
        if (cartList.containsKey(productDto)) {
            cartList.remove(productDto);
        }
    }

    public void clearAllProductFromCart() {
        cartList.clear();
    }

}
