package com.meretskiy.internet.market.controllers;

import com.meretskiy.internet.market.beans.JwtTokenUtil;
import com.meretskiy.internet.market.dto.JwtRequest;
import com.meretskiy.internet.market.dto.JwtResponse;
import com.meretskiy.internet.market.dto.UserDto;
import com.meretskiy.internet.market.exceptions_handling.MarketError;
import com.meretskiy.internet.market.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
    Контроллер для аутентификации с эндпоинтом, который позволяет проверить клиента
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    //специальный бин для того чтобы парсить токены.
    private final JwtTokenUtil jwtTokenUtil;
    //чтобы провести аутентификацию
    private final AuthenticationManager authenticationManager;

    //нам прилетает JwtRequest в виде json
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            //по JwtRequest формируем токен и через authenticationManager делаем аутентификацию(проверяем логин и пароль)
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new MarketError(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        //если аутентификация прошла успешно, то из базы достаем настоящего пользователя
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        //и для него генерим токен
        String token = jwtTokenUtil.generateToken(userDetails);
        //возвращаем клиенту в респонсе этот токен
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/auth/reg")
    public ResponseEntity<?> createNewUser(@RequestBody UserDto userDto) {
        try {
            userService.createNewUser(userDto);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MarketError(HttpStatus.BAD_REQUEST.value(), "Incorrect username or email"), HttpStatus.BAD_REQUEST);
        }
    }
}
