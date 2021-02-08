package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.dto.OrderDto;
import com.meretskiy.internet.market.exceptions_handling.ResourceNotFoundException;
import com.meretskiy.internet.market.model.User;
import com.meretskiy.internet.market.services.OrderService;
import com.meretskiy.internet.market.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    //создаем заказ
    @GetMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrderFromCart(Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        orderService.createFromUserCart(user);
    }

    //показать все заказы
    @GetMapping
    public List<OrderDto> getCurrentUserOrders(Principal principal) {
        return orderService.findAllOrdersByOwnerName(principal.getName()).stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
