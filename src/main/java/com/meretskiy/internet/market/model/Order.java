package com.meretskiy.internet.market.model;

import com.meretskiy.internet.market.beans.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "order")
    //включаем каскадирование (при изменении заказа, OrderItem тоже должны быть изменены)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "price")
    private int price;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //собираем заказ из корзины конкретного юзера
    public Order(Cart cart, User user, String deliveryAddress) {
        this.items = new ArrayList<>();
        this.owner = user;
        this.price = cart.getTotalPrice();
        this.deliveryAddress = deliveryAddress;
        cart.getItems().stream().forEach((oi) -> {
            oi.setOrder(this);
            items.add(oi);
        });
    }
}

