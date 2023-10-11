package com.marcosmatheus.sb.servicebus.topic;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final SendMessage sendMessage;

    public TopicController(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDTO messageDTO) {
        sendMessage.sendMessage(messageDTO.message());
    }
}
