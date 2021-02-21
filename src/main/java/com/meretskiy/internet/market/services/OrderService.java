package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.beans.Cart;
import com.meretskiy.internet.market.model.Order;
import com.meretskiy.internet.market.model.User;
import com.meretskiy.internet.market.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final Cart cart;

    public Order createFromUserCart(User user, String address) {
        Order order = new Order(cart, user, address);
        order = orderRepository.save(order);
        //только после того как заказ сохранился в базу можем почистить корзину
        cart.clear();
        return order;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAllOrdersByOwnerName(String username) {
        return orderRepository.findAllByOwnerUsername(username);
    }
}
