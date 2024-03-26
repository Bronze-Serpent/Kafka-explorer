package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Component
public class MyKafkaProducer
{
    @Value("${kafka.topic.name}")
    private final String topicName;

    private final KafkaTemplate<String, Lada> kafkaTemplate;


    @Scheduled(cron = "*/2 * * * * *")
    public void sendMessage()
    {
        Lada lada = new Lada(UUID.randomUUID(), "Granta", Attempt.TO_DRIVE_BADLY);
        log.info("Sending data: {}", lada);

        ProducerRecord<String, Lada> record = new ProducerRecord<>(topicName,"key", lada);

        kafkaTemplate.send(record);
    }
}
