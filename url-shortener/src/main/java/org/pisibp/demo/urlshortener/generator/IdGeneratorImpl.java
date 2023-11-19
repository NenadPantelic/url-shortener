package org.pisibp.demo.urlshortener.generator;


import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGeneratorImpl implements IdGenerator {

    private static final AtomicLong ID_COUNTER = new AtomicLong(System.currentTimeMillis());

    public long getNext() {
        return ID_COUNTER.getAndIncrement();
    }
}
