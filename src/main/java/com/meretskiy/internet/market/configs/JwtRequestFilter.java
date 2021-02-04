package com.meretskiy.internet.market.configs;

import com.meretskiy.internet.market.beans.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/*
    Наш кастомный фильтр, который мы встраиваем в цепочку стандартных spring security фильтров.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
//    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    /*
        Обрабатываем пролетающий через фильтр запрос.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //проверяем есть ли в запросе хедер Authorization
        String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        //если такой хедер существует и он начинается со слова Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //отпиливаем лишнее оставив только токен
            jwt = authHeader.substring(7);
            try {
                //достаем из токена имя пользователя, при этом происходит проверка жизни токена и может прилетель exception
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (ExpiredJwtException e) {
                log.debug("The token is expired");
//                String error = JsonUtils.convertObjectToJson(new BookServiceError(HttpStatus.UNAUTHORIZED.value(), "Jwt is expired"));
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, error);
//                return;
            }
        }

        //если пользователь существует и контекст пустой
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //вариант 1: каждый раз дергаем базу при запросах мульт запросов - мульт обращений к бд
            //достаем пользователя из базы
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //по тому что достали формируем токен
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            //кладем пользователя в контекст
//            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(token);

            //вариант 2: считаем раз пользователь токен получил и у него есть имя и роли значит ему верим. нет лишних обращений к бд
            // минус в том, что если заменили роль, пока токен действует пользователь может авторизоваться...
            //формируем новый токен с именем пользователя и списком его ролей, пароль зануляем.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, jwtTokenUtil.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            //в ручную кладем информацию о пользователе в spring security context в обход стартных механизмов
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
