package com.javamonks.process;

import org.springframework.messaging.Message;

public interface IntegrationProcess<T> {

    public abstract Message<T> doProcess(Message<?> message);
}
