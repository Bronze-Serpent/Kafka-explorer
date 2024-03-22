package com.barabanov;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MyKafkaConsumer
{

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.groupId}")
    public void LadaConsumer(ConsumerRecord<String, Cart> consumerRecord)
    {
        log.info("Received record: {}", consumerRecord);
        Cart value = consumerRecord.value();
        log.info("Lada here: {}", value);
    }
}
