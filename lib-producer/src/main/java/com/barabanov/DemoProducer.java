package com.barabanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;


@Slf4j
public class DemoProducer {


    public static void main(String[] args) throws InterruptedException {
        KafkaProducer<String, String> kafkaProducer = KafkaProducerFactory.buildKafkaProducer("localhost:9092");
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaSender kafkaSender = new KafkaSender(objectMapper, kafkaProducer);

        for (int i = 2_000_000; i < 3_000_000; i++)
            kafkaSender.sendSimpleMsg(new SimpleMsg(i, "Моё сообщение с id: " + i));
//        Thread.sleep(170_000);
//
//        for (int i = 1_000_000; i < 2_000_000; i++)
//            kafkaSender.sendSimpleMsg(new SimpleMsg(i, "Моё сообщение с id: " + i));

        // нужно очистить сообщения в топике между poll, пока ведётся обработка первых 5 сообщений и записать в топик новые сообщения
    }
}
