package com.barabanov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class SimpleMsgHandler implements KafkaMsgHandler<SimpleMsg> {


    @Override
    public void handleMsg(SimpleMsg msg) {
        log.info("Обработано сообщение из kafka с id: {}", msg.id());
    }
}
