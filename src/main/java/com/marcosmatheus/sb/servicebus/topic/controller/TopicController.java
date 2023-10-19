package com.marcosmatheus.sb.servicebus.topic.controller;

import com.marcosmatheus.sb.servicebus.topic.consumer.Consumer;
import com.marcosmatheus.sb.servicebus.topic.publisher.MessageDTO;
import com.marcosmatheus.sb.servicebus.topic.publisher.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final Publisher publisher;
    private final Consumer consumer;

    public TopicController(Publisher sendMessage, Consumer receiver) {
        this.publisher = sendMessage;
        this.consumer = receiver;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDTO messageDTO) {
        publisher.publishMessage(messageDTO.message());
    }

    @GetMapping("/receive")
    public void receive(@RequestBody MessageDTO messageDTO) throws InterruptedException {
        consumer.consumeMessages();
    }
}
