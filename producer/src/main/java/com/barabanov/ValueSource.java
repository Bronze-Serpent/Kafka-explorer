package com.barabanov;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;


@AllArgsConstructor
public abstract class ValueSource<T>
{
    protected final Consumer<T> valueConsumer;

    public abstract void generate();
}
