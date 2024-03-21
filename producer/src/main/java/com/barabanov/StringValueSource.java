package com.barabanov;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.concurrent.TimeUnit;


public class StringValueSource extends ValueSource<StringValue>
{
    private final AtomicLong valueNum = new AtomicLong(1);

    public StringValueSource(Consumer<StringValue> valueConsumer)
    {
        super(valueConsumer);
    }


    @Override
    public void generate()
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(
                () ->  this.valueConsumer.accept(makeVal()),
                0,
                1,
                TimeUnit.SECONDS
        );
    }


    private StringValue makeVal()
    {
        long num = valueNum.incrementAndGet();
        return new StringValue(num, "Value with num " + num);
    }

}
