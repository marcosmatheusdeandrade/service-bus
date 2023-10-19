package com.marcosmatheus.sb.servicebus.topic;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendMessage {

    @Value("${topic}")
    public String topicName;

    @Value("${connectionString}")
    private String connectionString;

    void sendMessage(String message) {
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .topicName(topicName)
                .buildClient();

        senderClient.sendMessage(new ServiceBusMessage(message));
    }
}