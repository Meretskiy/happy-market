package com.meretskiy.internet.market.repositories;

import com.meretskiy.internet.market.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //поиск в базе всех заказов по имени текущего пользователя
    List<Order> findAllByOwnerUsername(String ownerUsername);
}
