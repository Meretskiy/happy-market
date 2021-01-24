package com.meretskiy.internet.market.exceptions_handling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//аннтоация @ControllerAdvice позволяет использовать этот код во всех контроллерах
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    //кастомный полуглобальный спринговый обработчик исключений
    //если мы где то кидаем ResourceNotFoundException, то этот метод его перехватывает и обрабатывает
    @ExceptionHandler
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        //обертываем в json и отправляем клиенту
        MarketError error = new MarketError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
