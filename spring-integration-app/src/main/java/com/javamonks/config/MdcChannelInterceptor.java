package com.javamonks.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
    @GlobalChannelInterceptor(patterns = {"*"})
    public class MdcChannelInterceptor implements ChannelInterceptor {
        private static final Logger logger = LoggerFactory.getLogger(com.javamonks.config.MdcChannelInterceptor.class);

        private static final String CORRELATION_ID_HEADER = "correlationId";

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            MessageHeaders messageHeaders = message.getHeaders();
            if (!messageHeaders.containsKey(CORRELATION_ID_HEADER)) {
                // If the message does not have a correlation ID, add one
                String correlationId = UUID.randomUUID().toString();

                // Optional: put into MDC for logging purposes
                MDC.put(CORRELATION_ID_HEADER, correlationId);

                MessageHeaderAccessor accessor = MessageHeaderAccessor.getMutableAccessor(message);
                accessor.setHeader(CORRELATION_ID_HEADER, correlationId);
                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            } else {
                // If it already exists (e.g., propagated from an external source), ensure it's in MDC
                MDC.put(CORRELATION_ID_HEADER, messageHeaders.get(CORRELATION_ID_HEADER).toString());
            }
            return message; // returns the original message if no change needed
        }

        @Override
        public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
            // Clean up the MDC after the send operation completes
            MDC.remove(CORRELATION_ID_HEADER);
        }
}

