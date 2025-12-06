/*
package com.javamonks.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Service;

    @Service
    @GlobalChannelInterceptor(patterns = {"*"})
    public class MdcChannelInterceptor implements ChannelInterceptor {
        private static final Logger logger = LoggerFactory.getLogger(com.javamonks.config.MdcChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Add message headers into MDC
       */
/* if (message.getHeaders().getId() != null) {
            MDC.put("messageId", message.getHeaders().getId().toString());
        }*//*

        if (message.getHeaders().get("correlationId") != null) {
            MDC.put("correlationId", (String) message.getHeaders().get("correlationId"));
        }
        return message;
    }

    @Override
    public void afterSendCompletion(
            Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // Clean up MDC to avoid leaks
//        MDC.remove("messageId");
        MDC.remove("correlationId");
    }
}

*/
