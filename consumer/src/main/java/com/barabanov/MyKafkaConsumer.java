package com.barabanov;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.*;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginCallbackHandler.CLIENT_ID_CONFIG;


@Slf4j
public class MyKafkaConsumer
{
    private final KafkaConsumer<String, String> kafkaConsumer;

    public static final String TOPIC_NAME = "MyTopic";


    public MyKafkaConsumer(String bootstrapServers)
    {
        Properties props = new Properties();

        // аналогично producer
        props.put(RETRIES_CONFIG, 1);
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // можно задать здесь, а можно в запросе Poll.
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        props.put(GROUP_ID_CONFIG, "myKafkaConsumerGroup"); // id группы потребителей
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true"); // разрешает периодическое сохранение offset у consumer-a
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100"); // устанавливает период автоматического коммита offset
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest"); // Устанавливает первоначальный дефолтный оффсет, если такой не задан по умолчанию.
        // earliest- значит начнём читать с начала топика. latest - начнём читать  с последнего сообщения в топике
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(List.of(TOPIC_NAME));
    }


    public KafkaConsumer<String, String> getConsumer()
    {
        return kafkaConsumer;
    }
}
