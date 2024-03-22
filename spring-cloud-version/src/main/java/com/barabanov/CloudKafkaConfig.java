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

    // TODO: 22.03.2024 А в какие моменты и зачем вызывается этот supplier
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


    @Bean
    public Consumer<Lada> ladaConsumer()
    {
        return lada-> log.info("Consuming Lada:{}", lada);
    }
}
