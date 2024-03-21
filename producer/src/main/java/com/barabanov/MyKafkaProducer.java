package com.barabanov;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginCallbackHandler.CLIENT_ID_CONFIG;


@Slf4j
public class MyKafkaProducer
{
    private final KafkaProducer<String, String> kafkaProducer;

    public MyKafkaProducer(String bootstrapServers)
    {
        Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, "myKafkaProducer");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); //список серверов, к которым клиент Kafka должен подключиться для инициализации связи с кластером Kafka.
        // Этот параметр указывает на один или несколько брокеров Kafka, которые будут использоваться клиентом для обнаружения и установления соединения с кластером.
        // При запуске клиента Kafka, он использует сервера из bootstrap.servers для инициализации соединения с кластером.
        // Затем клиент получает дополнительную информацию о топиках и брокерах в кластере
        props.put(ACKS_CONFIG, "1"); // Когда считается выполненным запрос. Когда все реплики сохранили копии или когда только какое-то число.
//        Без параметров не будет подтверждения вообще. -1 - когда все реплики сохранят
//        (В данном примере всего 1 брокер и 1 партиция так что это и неважно)
        props.put(RETRIES_CONFIG, 1); // сколько будет повторных попыток отправки / получения
        props.put(BATCH_SIZE_CONFIG, 16384); // Сообщения сначала сохраняются на уровне java, а фактически отправляются пачками. Это задание размера отправляемой пачки
        props.put(LINGER_MS_CONFIG, 10); // Время через которое будут отправляться пачки, независимо от того набралась ли полная пачка. Если пачка набралась раньше, то и отправится раньше.
        props.put(BUFFER_MEMORY_CONFIG, 33_554_432); // общий объем памяти, который брокер Kafka может использовать для всех своих соединений
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // время на которое производитель или потребитель может блокироваться при отправке или получении запроса.
        // Этот параметр позволяет контролировать поведение клиентов Kafka в случае возникновения задержек или проблем с доступом к брокеру
        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"); // сериализация для ключа
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"); // сериализация для сообщения

        kafkaProducer = new KafkaProducer<>(props);

        // добавляем новый обработчик завершения работы виртуальной машины, чтобы закрыть ресурсы
        var shutdownHook = new Thread(() -> {
            log.info("closing kafka producer");
            kafkaProducer.close();
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }


    public KafkaProducer<String, String> getProducer()
    {
        return kafkaProducer;
    }
}
