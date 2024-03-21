package com.barabanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class DemoProducer
{

    public static void main(String[] args) {
        var kafkaProducer = new MyKafkaProducer("localhost:9092");

        var kafkaDataSender = new KafkaDataSender(new ObjectMapper(), kafkaProducer);

        var valueSource = new StringValueSource(kafkaDataSender::send);
        valueSource.generate();
    }
}
