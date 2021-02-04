package com.meretskiy.internet.market.dto;

import lombok.Data;

/*
    Вся информация о пользователе передается в виде json объектов.
 */
@Data
public class JwtRequest {
    private String username;
    private String password;
}
