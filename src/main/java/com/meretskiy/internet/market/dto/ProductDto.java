package com.meretskiy.internet.market.dto;

import com.meretskiy.internet.market.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private int price;

    //ручной маппинг из product в productDto пока делаем просто в конструкторе
    public ProductDto(Product p) {
        this.id = p.getId();
        this.title = p.getTitle();
        this.price = p.getPrice();
    }
}
