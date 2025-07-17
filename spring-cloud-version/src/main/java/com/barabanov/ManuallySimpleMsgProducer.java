package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Component
public class ManuallySimpleMsgProducer {

    private final StreamBridge streamBridge;


    @Scheduled(cron = "*/5 * * * * *")
    public void manuallySendLada()
    {
        long id = ThreadLocalRandom.current().nextLong();
        SimpleMsg simpleMsg = new SimpleMsg(id, "Моё сообщение для сообщения с id" + id);

        log.info("Отправка сделанного вручную simpleMsg с id: {}", id);
        streamBridge.send("simpleMsgSupplier-out-0", simpleMsg);
    }
}
