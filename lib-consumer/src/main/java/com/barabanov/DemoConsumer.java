package com.barabanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;

import static com.barabanov.KafkaConsumerFactory.SIMPLE_MSG_TOPIC_NAME;


@Slf4j
public class DemoConsumer {

    public static void main(String[] args) {
        KafkaConsumer<String, String> kafkaConsumer = KafkaConsumerFactory.buildKafkaConsumer("localhost:9092");
        kafkaConsumer.subscribe(List.of(SIMPLE_MSG_TOPIC_NAME));
        KafkaContainer<SimpleMsg> simpleKafkaMsgContainer = new KafkaContainer<>(kafkaConsumer, new ObjectMapper(), new SimpleMsgHandler(), SimpleMsg.class);

        simpleKafkaMsgContainer.consumeMessageFromKafka();
    }
}
