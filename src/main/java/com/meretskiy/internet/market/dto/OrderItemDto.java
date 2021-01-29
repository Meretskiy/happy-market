package com.meretskiy.internet.market.dto;

import com.meretskiy.internet.market.model.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private String productTitle;
    private int quantity;
    private int pricePerProduct;
    private int price;

    public OrderItemDto(OrderItem orderItem) {
        this.productTitle = orderItem.getProduct().getTitle();
        this.quantity = orderItem.getQuantity();
        this.pricePerProduct = orderItem.getPricePerProduct();
        this.price = orderItem.getPrice();
    }
}
