package com.meretskiy.internet.market.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


/*
    Ответ с токеном.
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
