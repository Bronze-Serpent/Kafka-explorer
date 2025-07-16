package com.barabanov;


import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;


public class SimpleMsgGenerator extends AbstractMsgGenerator<SimpleMsg> {
    private final AtomicLong idSequence = new AtomicLong(1);

    public SimpleMsgGenerator(Consumer<SimpleMsg> msgConsumer) {
        super(msgConsumer);
    }


    @Override
    protected SimpleMsg createVal() {
        long id = idSequence.incrementAndGet();
        return new SimpleMsg(id, "Message with id " + id);
    }

}
