package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMsgConsumer {


    // + с помощью containerPostProcessor указать commonErrorHandler, если подход по умолчанию не подходит
    @KafkaListener(
            topics = "${kafka-topics.simple-msg-topic-name}",
            concurrency = "${kafka-topics.simple-msg-topic-concurrency}",
            properties = {"spring.json.value.default.type = com.barabanov.SimpleMsg"})
    public void listenSimpleMsg(SimpleMsg msg) {
        log.info("Было получено сообщение из kafka с id: {}", msg.id());

        // вызов сервиса высокого уровня, куда передаётся сообщение на обработку
    }
}
