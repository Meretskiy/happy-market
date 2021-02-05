package com.meretskiy.internet.market.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "items")
    private List<OrderItem> items;

    @Column(name = "total_prices")
    private int totalPrice;

    @Column(name = "user")
    private User user;
}

