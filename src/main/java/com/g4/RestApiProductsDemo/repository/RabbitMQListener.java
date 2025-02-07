package com.g4.RestApiProductsDemo.repository;

import com.g4.RestApiProductsDemo.RabbitConfig.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RabbitMQListener implements ChannelAwareMessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(Message message, Channel channel) throws Exception {
        String messageBody = new String(message.getBody());
        System.out.println("Received message: " + messageBody);

        // Wait for a random time between 3000 ms to 10000 ms
//        Random random = new Random();
//        int sleepTime = 3000 + random.nextInt(7001); // Generates a random number between 3000 and 10000
//
//        try {
//            Thread.sleep(sleepTime);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        // Upload the message back to RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, messageBody);
        System.out.println("Message re-queued: " + messageBody);

        // Manually acknowledge the message
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}