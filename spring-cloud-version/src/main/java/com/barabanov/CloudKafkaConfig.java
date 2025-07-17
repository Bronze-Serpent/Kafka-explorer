package com.barabanov;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class CloudKafkaConfig {

    @Bean
    public Supplier<SimpleMsg> simpleMsgSupplier() {
        return () ->
        {
            long id = ThreadLocalRandom.current().nextLong();
            SimpleMsg simpleMsg = new SimpleMsg(id, "Моё сообщение для сообщения с id" + id);

            log.info("Отправка simpleMsg с id: {}", id);
            return simpleMsg;
        };
    }


    @Bean
    public Consumer<SimpleMsg> simpleMsgConsumer() {
        return simpleMsg ->
        {

            log.info("Обрабатываю simpleMsg с id:{}", simpleMsg.id());
        };
    }
}
