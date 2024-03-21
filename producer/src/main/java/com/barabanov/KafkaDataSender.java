package com.barabanov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;



@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class KafkaDataSender
{
    private final ObjectMapper mapper;
    private final MyKafkaProducer kafkaProducer;


    public void send(StringValue value)
    {
        try {
            String valueAsStr = mapper.writeValueAsString(value);

            kafkaProducer.getProducer()
                    .send(new ProducerRecord<>(Long.toString(value.id()), valueAsStr), new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception != null)
                                log.error("message wasn't sent", exception);
                            else
                                log.info("message id:{} was sent, offset:{}", value.id(), metadata.offset());
                        }
                    });
        } catch (JsonProcessingException e)
        {
            log.error("error during parsing message", e);
            throw new RuntimeException(e);
        }
    }
}
