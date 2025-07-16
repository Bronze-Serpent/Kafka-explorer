package com.barabanov;

public interface KafkaMsgHandler<V> {

    void handleMsg(V msg);
}
