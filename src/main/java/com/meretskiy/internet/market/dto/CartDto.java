package com.meretskiy.internet.market.dto;

import com.meretskiy.internet.market.beans.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CartDto {
    private List<OrderItemDto> items;
    private int totalPrice;

    public CartDto(Cart cart) {
        this.items = cart.getItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
        this.totalPrice = cart.getTotalPrice();
    }
}
