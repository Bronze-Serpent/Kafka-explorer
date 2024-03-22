package com.barabanov;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DemoConsumer
{

    public static void main(String[] args)
    {
        MyKafkaConsumer kafkaConsumer = new MyKafkaConsumer("localhost:9092");

        KafkaDataReceiver kafkaDataReceiver = new KafkaDataReceiver(new ObjectMapper(), kafkaConsumer);
        kafkaDataReceiver.dataHandler();
    }
}
