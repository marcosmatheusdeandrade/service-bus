package com.marcosmatheus.sb.servicebus.topic.consumer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusFailureReason;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Value("${subName}")
    public String subName;

    @Value("${topic}")
    public String topicName;

    @Value("${connectionString}")
    private String connectionString;

    public void consumeMessages() throws InterruptedException {
        CountDownLatch countdownLatch = new CountDownLatch(1);

        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .topicName(topicName)
                .subscriptionName(subName)
                .processMessage(this::processMessage)
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        LOGGER.debug("Starting the processor");
        processorClient.start();

        TimeUnit.SECONDS.sleep(10);
        LOGGER.debug("Stopping and closing the processor");
        processorClient.close();
    }

    private void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        LOGGER.info("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(), message.getSequenceNumber(), message.getBody());
    }

    private void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
        LOGGER.error("Error when receiving messages from namespace: '%s'. Entity: '%s'%n", context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            LOGGER.error("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        LOGGER.error("Error source %s, reason %s, message: %s%n", context.getErrorSource(), reason, context.getException());
    }
}
