package com.meretskiy.internet.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//указываем путь к еще одному файлу настроек, где будем хранить секретный ключ
@PropertySource("classpath:secured.properties")
public class HappyMarketApplication {


    public static void main(String[] args) {
        SpringApplication.run(HappyMarketApplication.class, args);
    }

}
