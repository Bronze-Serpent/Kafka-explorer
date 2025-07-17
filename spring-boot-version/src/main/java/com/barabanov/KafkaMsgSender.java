package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMsgSender {

    @Value("${kafka-topics.simple-msg-topic-name}")
    private final String simpleMsgTopicName;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendSimpleMsg(SimpleMsg simpleMsg) {
        log.info("Отправляется SimpleMsg с id {}", Optional.ofNullable(simpleMsg)
                .map(SimpleMsg::id)
                .orElse(null));

        kafkaTemplate.send(simpleMsgTopicName, simpleMsg);
    }
}
