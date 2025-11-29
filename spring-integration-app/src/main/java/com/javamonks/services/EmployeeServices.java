package com.javamonks.services;

import com.javamonks.model.Employee;
import org.springframework.integration.annotation.*;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServices {

    @ServiceActivator(inputChannel = "req-emp-name-channel")
    public void getEmployeeName(Message<String> name) {
        MessageChannel replyChannel = (MessageChannel) name.getHeaders().getReplyChannel();
        replyChannel.send(name);
    }

    @ServiceActivator(inputChannel = "req-hire-emp-channel", outputChannel = "process-emp-channel")
    public Message<Employee> hireEmployee(Message<Employee> employee) {
        return employee;
    }

    @ServiceActivator(inputChannel = "process-emp-channel", outputChannel = "get-emp-channel")
    public Message<Employee> processEmployee(Message<Employee> employee) {
        employee.getPayload().setStatus("PERMANENT");
        return employee;
    }

    @ServiceActivator(inputChannel = "get-emp-channel")
    public void getEmployeeStatus(Message<Employee> employee) {
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        replyChannel.send(employee);
    }

    //#########################  TRANSFORMER  ########################//

    @Transformer(inputChannel = "emp-status-channel", outputChannel = "output-channel")
    public Message<String> convertToUppercase(Message<String> message) {

        String payload = message.getPayload();
        Message<String> messageInUppercase = MessageBuilder.withPayload(payload.toUpperCase())
                .copyHeaders(message.getHeaders())
                .build();
        return messageInUppercase;
    }

    //#########################  SPLITTER  ########################//
    @Splitter(inputChannel = "emp-managers-channel", outputChannel = "output-channel")
    List<Message<String>> splitMessage(Message<?> message) {
        List<Message<String>> messages = new ArrayList<Message<String>>();
        String[] msgSplits = message.getPayload().toString().split(",");

        for (String split : msgSplits) {
            Message<String> msg = MessageBuilder.withPayload(split)
                    .copyHeaders(message.getHeaders())
                    .build();
            messages.add(msg);
        }

        return messages;
    }

    //#########################  FILTER  ########################//

    @Filter(inputChannel = "dev-emp-channel", outputChannel = "output-channel")
    boolean filter(Message<?> message) {
        String msg = message.getPayload().toString();
        return msg.contains("Dev");
    }

    //#########################  COMMON OUTPUT CHANNEL  ########################//

    @ServiceActivator(inputChannel = "output-channel")
    public void consumeStringMessage(Message<String> message) {
        System.out.println("Received message from output channel : " + message.getPayload());
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        replyChannel.send(message);
    }

    //#########################  FILTER  ########################//

    @Router(inputChannel = "emp-dep-channel")
    String getEmployeeDepartment(Message<Employee> message) {
        String depRoute = null;
        switch (message.getPayload().getDepartment()) {
            case "SALES":
                depRoute = "sales-channel";
                break;
            case "MARKETING":
                depRoute = "marketing-channel";
                break;
        }

        return depRoute;
    }

    @ServiceActivator(inputChannel = "sales-channel")
    public void salesChannel(Message<Employee> employee) {
        Message<String> reply = MessageBuilder.withPayload("SALES DEPARTMENT").build();
        System.out.println("Recievied sales: " + reply.toString());
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        replyChannel.send(employee);
    }

    @ServiceActivator(inputChannel = "marketing-channel")
    public void marketingChannel(Message<Employee> employee) {
        Message<String> reply = MessageBuilder.withPayload("Marketing DEPARTMENT").build();
        System.out.println("Recievied sales: " + reply.toString());
        MessageChannel replyChannel = (MessageChannel) employee.getHeaders().getReplyChannel();
        replyChannel.send(employee);
    }


}
