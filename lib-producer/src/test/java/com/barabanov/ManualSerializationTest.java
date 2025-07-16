package com.barabanov;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.LongStream;

import static com.barabanov.KafkaSender.SIMPLE_MSG_TOPIC_NAME;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.assertj.core.api.Assertions.assertThat;


class ManualSerializationTest extends KafkaBase {

    @Test
    void shouldWriteAndReadStringsFromKafka() {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaProducer<String, String> kafkaProducer = KafkaProducerFactory.buildKafkaProducer(this.getBootstrapServers());
        KafkaSender kafkaDataSender = new KafkaSender(objectMapper, kafkaProducer);

        List<SimpleMsg> simpleMsgs = LongStream.range(0, 9).boxed()
                .map(idx -> new SimpleMsg(idx, "test:" + idx))
                .toList();
        // кладём в кафку сообщения
        simpleMsgs.forEach(kafkaDataSender::sendSimpleMsg);

        List<SimpleMsg> receivedObjects = new LinkedList<>();

        // создаём consumer-a, который вытащит все значения
        try (KafkaConsumer<String, String> consumer = createConsumer(this.getBootstrapServers())) {
            consumer.poll(Duration.ofMillis(1000))
                    .records(SIMPLE_MSG_TOPIC_NAME)
                    .iterator()
                    .forEachRemaining((kafkaRecord) ->
                    {
                        try {
                            SimpleMsg readObject = objectMapper.readValue(kafkaRecord.value(), SimpleMsg.class);
                            receivedObjects.add(readObject);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        assertThat(receivedObjects).containsExactlyInAnyOrderElementsOf(simpleMsgs);
    }


    private KafkaConsumer<String, String> createConsumer(String bootstrapServers) {
        Properties props = new Properties();

        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        props.put(GROUP_ID_CONFIG, "myTestKafkaConsumerGroup");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(List.of(SIMPLE_MSG_TOPIC_NAME));

        return kafkaConsumer;
    }
}