package com.barabanov;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;


@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class KafkaDataReceiver
{
    ObjectMapper mapper;
    MyKafkaConsumer kafkaConsumer;
    Duration timeout = Duration.ofMillis(1000);
    AtomicBoolean stopFlag = new AtomicBoolean(false);


    public void dataHandler()
    {
        while (!stopFlag.get())
        {
            // TODO: 21.03.2024 Т.е. записи достаются батчем. А как доставать по 1? Или можно просто commit делать по 1
            ConsumerRecords<String, String> records = kafkaConsumer.getConsumer().poll(timeout);
            for (ConsumerRecord<String, String> kafkaRecord : records)
            {
                try {
                    var key = mapper.readValue(kafkaRecord.key(), Long.class);
                    var value = mapper.readValue(kafkaRecord.value(), StringValue.class);
                    log.info("Read message from kafka. Key:{}, value:{}, record:{}", key, value, kafkaRecord);
                } catch (JacksonException ex) {
                    log.error("can't parse record:{}", kafkaRecord, ex);
                }
            }
        }
    }

    public void setStopFlag(boolean flag) {
        stopFlag.set(flag);
    }
}
