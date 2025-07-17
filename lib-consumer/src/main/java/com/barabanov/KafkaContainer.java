package com.barabanov;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Slf4j
public class KafkaContainer<V> {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private final ObjectMapper mapper;
    private final KafkaMsgHandler<V> kafkaMsgHandler;
    private final Class<V> msgClass;

    private final Duration pollTimeout = Duration.ofMillis(1000);
    private final AtomicBoolean stopContainer = new AtomicBoolean(false);


    public void consumeMessageFromKafka() {
        while (!stopContainer.get()) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(pollTimeout);
            for (ConsumerRecord<String, String> kafkaRecord : records) {
                try {
                    V value = mapper.readValue(kafkaRecord.value(), msgClass);
                    log.info("Считано сообщение из кафки. Key:{}, value:{}", kafkaRecord.key(), value);
                    kafkaMsgHandler.handleMsg(value);
                } catch (JacksonException ex) {
                    log.error("Не удалась преобразовать запись в целевой класс:{}", kafkaRecord, ex);
                }
            }
        }
    }


    public void stopContainer() {
        stopContainer.set(true);
    }
}
