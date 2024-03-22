package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
public class ManuallyLadaProducer
{
    private final StreamBridge streamBridge;


    @Scheduled(cron = "*/2 * * * * *")
    public void manuallySendLada()
    {
        Lada lada = new Lada(UUID.randomUUID(), "Hand made Vesta", Attempt.TO_STAY);
        log.info("Sending an hand made lada:{}", lada);

        streamBridge.send("ladaSupplier-out-0", lada);
    }
}
