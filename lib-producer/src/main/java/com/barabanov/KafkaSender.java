package com.barabanov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


@RequiredArgsConstructor
@Slf4j
public class KafkaSender {
    public static final String SIMPLE_MSG_TOPIC_NAME = "SIMPLE_MSG_TOPIC";

    private final ObjectMapper objectMapper;
    private final KafkaProducer<String, String> kafkaProducer;


    public void sendSimpleMsg(SimpleMsg simpleMsg) {
        sendMsg(SIMPLE_MSG_TOPIC_NAME, String.valueOf(simpleMsg.id()), simpleMsg);
    }


    private void sendMsg(String topicName, String key, Object value) {
        {
            try {
                String valueAsStr = objectMapper.writeValueAsString(value);

                kafkaProducer.send(new ProducerRecord<>(topicName, key, valueAsStr), (metadata, exception) -> {
                            if (exception != null)
                                log.error("Сообщение не отправлено!", exception);
                            else
                                log.info("Сообщение с key:{} было отправлено, offset:{}", key, metadata.offset());
                        });
            } catch (JsonProcessingException e)
            {
                log.error("Ошибка при преобразовании сообщения", e);
                throw new RuntimeException(e);
            }
        }
    }
}
