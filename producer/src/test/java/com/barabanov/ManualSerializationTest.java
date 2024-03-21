package com.barabanov;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.LongStream;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.assertj.core.api.Assertions.assertThat;


class ManualSerializationTest extends KafkaBase
{

    @Test
    void shouldWriteAndReadStringsFromKafka()
    {
        List<StringValue> stringValues = LongStream.range(0, 9).boxed()
                .map(idx -> new StringValue(idx, "test:" + idx))
                .toList();
        ObjectMapper objectMapper = new ObjectMapper();

        var kafkaProducer = new MyKafkaProducer(getBootstrapServers());
        var kafkaDataSender = new KafkaDataSender(TOPIC_NAME, objectMapper, kafkaProducer);

        ValueSource<StringValue> valueSource = new ValueSource<>(kafkaDataSender::send)
        {
            @Override
            public void generate()
            {
                for (StringValue stringValue : stringValues)
                    this.valueConsumer.accept(stringValue);
            }
        };

        // кладём значения в кафку
        valueSource.generate();

        List<StringValue> receivedObjects = new LinkedList<>();

        // создаём consumer-a, который вытащит все значения
        try(KafkaConsumer<String, String> consumer = createConsumer(this.getBootstrapServers()))
        {
            consumer.poll(Duration.ofMillis(1000))
                    .records(TOPIC_NAME)
                    .iterator()
                    .forEachRemaining((kafkaRecord) ->
                    {
                        try
                        {
                            StringValue readObject = objectMapper.readValue(kafkaRecord.value(), StringValue.class);
                            receivedObjects.add(readObject);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        assertThat(receivedObjects).containsExactlyInAnyOrderElementsOf(stringValues);
    }


    private KafkaConsumer<String, String> createConsumer(String bootstrapServers)
    {
        Properties props = new Properties();

        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        props.put(GROUP_ID_CONFIG, "myKafkaConsumerGroup");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(List.of(TOPIC_NAME));

        return kafkaConsumer;
    }
}