package com.barabanov;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.barabanov.KafkaSender.SIMPLE_MSG_TOPIC_NAME;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;


@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class KafkaBase
{
    private final static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0"));

    @Getter
    private String bootstrapServers;


    @BeforeAll
    public void init() throws ExecutionException, InterruptedException, TimeoutException
    {
        start(List.of(new NewTopic(SIMPLE_MSG_TOPIC_NAME, 1, (short) 1)));
    }


    public void start(Collection<NewTopic> topics) throws ExecutionException, InterruptedException, TimeoutException
    {
        kafkaContainer.start();
        bootstrapServers = kafkaContainer.getBootstrapServers();

        log.info("topics creation...");
        try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)))
        {
            var topicsCreateResult = admin.createTopics(topics);

            // Дожидаемся создания тем
            // Без этого может получиться ситуация, когда мы обращаемся к ещё фактически не созданной теме
            for(var topicResult: topicsCreateResult.values().values())
                topicResult.get(10, TimeUnit.SECONDS);

        }
        log.info("topics created");
    }
}