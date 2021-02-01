package com.meretskiy.internet.market.beans;

import com.meretskiy.internet.market.exceptions_handling.ResourceNotFoundException;
import com.meretskiy.internet.market.model.OrderItem;
import com.meretskiy.internet.market.model.Product;
import com.meretskiy.internet.market.services.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class Cart {
    private final ProductService productService;
    private List<OrderItem> items;
    private int totalPrice;

    @PostConstruct
    public void init() {
        this.items = new ArrayList<>();
    }

    /*
    добавление товара в корзину
     */
    public void addToCart(Long id) {
        //проходим по всем позициям в корзине
        for (OrderItem o : items) {
            //если находим позицию с искомым id, то просто увеличиваем количество товаров в позиции
            if (o.getProduct().getId().equals(id)) {
                o.incrementQuantity();
                //пересчитываем стоимость заказов лежащих в корзине
                recalculate();
                return;
            }
        }
        //если в корзине данного продукта пока нет
        //создаем продукт по id, создаем новый orderItem по этому продукту и добавляем в items и
        //пересчитываем стоимость заказов лежащих в корзине
        Product p = productService.findProductById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find product with id: " + id + " (add to cart)"));
        OrderItem orderItem = new OrderItem(p);
        items.add(orderItem);
        recalculate();
    }

    public void clearItem(String productTitle) {
        Iterator<OrderItem> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getProduct().getTitle().equals(productTitle)) {
                it.remove();
                recalculate();
                break;
            }
        }
    }

    public void clear() {
        items.clear();
        recalculate();
    }

    public void recalculate() {
        totalPrice = 0;
        for (OrderItem o : items) {
            totalPrice += o.getPrice();
        }
    }

    public void incrementQuantity(String productTitle) {
        for (OrderItem o : items) {
            if (o.getProduct().getTitle().equals(productTitle)) {
                o.incrementQuantity();
                recalculate();
                return;
            }
        }
    }

    public void decrementQuantity(String productTitle) {
        for (OrderItem o : items) {
            if (o.getProduct().getTitle().equals(productTitle)) {
                o.decrementQuantity();
                recalculate();
                return;
            }
        }
    }
}

