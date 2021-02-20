package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.dto.OrderDto;
import com.meretskiy.internet.market.dto.ProductDto;
import com.meretskiy.internet.market.exceptions_handling.ResourceNotFoundException;
import com.meretskiy.internet.market.model.User;
import com.meretskiy.internet.market.services.OrderService;
import com.meretskiy.internet.market.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    //создаем заказ с адресом доставки
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrderFromCart(Principal principal, @RequestBody String deliveryAddress) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        orderService.createFromUserCart(user, deliveryAddress);
    }

    //показать все заказы
    @GetMapping
    public List<OrderDto> getCurrentUserOrders(Principal principal) {
        return orderService.findAllOrdersByOwnerName(principal.getName()).stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
