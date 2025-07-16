package com.barabanov;

import lombok.AllArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


@AllArgsConstructor
public abstract class AbstractMsgGenerator<T> {
    protected final Consumer<T> msgConsumer;


    public void generate() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(
                () -> this.msgConsumer.accept(createVal()),
                0,
                5,
                TimeUnit.SECONDS
        );
    }

    protected abstract T createVal();
}
