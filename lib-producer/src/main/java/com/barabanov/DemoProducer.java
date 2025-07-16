package com.barabanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;


@Slf4j
public class DemoProducer {


    public static void main(String[] args) {
        KafkaProducer<String, String> kafkaProducer = KafkaProducerFactory.buildKafkaProducer("localhost:9092");
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaSender kafkaSender = new KafkaSender(objectMapper, kafkaProducer);

        AbstractMsgGenerator<SimpleMsg> msgGenerator = new SimpleMsgGenerator(kafkaSender::sendSimpleMsg);
        msgGenerator.generate();
    }
}
