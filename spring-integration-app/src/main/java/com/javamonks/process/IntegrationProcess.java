package com.javamonks.process;

import org.springframework.messaging.Message;

public interface IntegrationProcess {

    public abstract Message<?> doProcess(Message<?> message);
}
