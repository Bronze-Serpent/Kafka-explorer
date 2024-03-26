package com.barabanov;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Slf4j
@Configuration
public class CloudKafkaConfig
{

    @Bean
    public Supplier<Lada> ladaSupplier()
    {
        return () ->
        {
            Lada lada = new Lada(UUID.randomUUID(), "Vesta", Attempt.TO_DRIVE_BADLY);
            log.info("Sending Lada:{}", lada);
            return lada;
        };
    }
//    todo как обработать ошибки, возникающие при отправке в кафку через Supplier.


    @Bean
    public Consumer<Lada> ladaConsumer()
    {
        return lada->
        {
            //dead letter queue можно настроить в spring cloud. Туда автоматически падают письма, при обработке которых возникла ошибка
//            throw new RuntimeException("Я кинул ошибку " + lada.uuid());

            log.info("Вот моя лада:{}", lada);
        };
    }
}
