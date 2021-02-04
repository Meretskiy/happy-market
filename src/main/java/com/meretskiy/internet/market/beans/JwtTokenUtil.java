package com.meretskiy.internet.market.beans;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
    Бин для того чтобы парсить токены(создавать, разбирать на части)
 */
@Component
public class JwtTokenUtil {
    //для генерации токенов необходим специальный ключ, который знаем только мы.
    //этот ключ обычно харниться в отдельном файле и указываем его в аннотации @Value
    @Value("${jwt.secret}")
    private String secret;

    //формируем токен из UserDetails(берем информацию конкретного пользователя и для него создаем токен)
    public String generateToken(UserDetails userDetails) {
        //claims - это полезные данные которые нужно передать клиенту в токене
        Map<String, Object> claims = new HashMap<>();
        //берем пользователя, проходимся по списку его ролей и собираем в rolesList.
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        //лист кладем в мапу под ключем roles
        claims.put("roles", rolesList);

        //запрос текущей даты формирования токена
        Date issuedDate = new Date();
        //создаем время жизни токена
        Date expiredDate = new Date(issuedDate.getTime() + 20 * 60 * 1000);
        //с помощью jsonwebtoken библиотеки в билдер закидываем все эти данные, делаем подпись и формируем токен
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                //в подписи указываем алгоритм хеширования и наш ключ
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    /*
        Запрос имени пользователя из токена
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /*
        Запрос роли пользователя из токена
     */
    public List<String> getRoles(String token) {
        return getClaimFromToken(token, (Function<Claims, List<String>>) claims -> claims.get("roles", List.class));
    }

    /*
        Достаем из токена конкретный элемент
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /*
        Достает все данные из токена
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                //проверка подписи, потом проверяется что токен никто не подменил и время жизни токена не истекло
                .setSigningKey(secret)
                //парсим токен
                .parseClaimsJws(token)
                .getBody();
    }
}

