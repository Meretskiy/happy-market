package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.services.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;

    @GetMapping
    public Map<ProductDto, Integer> showCart() {
        return cart.showCart();
    }

    @GetMapping("/add/{id}")
    public void addProductToCart(@PathVariable Long id) {
        cart.addProductToCart(id);
    }

    @GetMapping("/clear/{id}")
    public void clearProductFromCart(@PathVariable Long id) {
        cart.clearProductFromCart(id);
    }

    @GetMapping("/clear")
    public void clearAllProductFromCart() {
        cart.clearAllProductFromCart();
    }
}
