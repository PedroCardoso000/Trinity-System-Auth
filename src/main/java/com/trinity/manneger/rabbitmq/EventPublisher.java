package com.trinity.manneger.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, Object event) {
        rabbitTemplate.convertAndSend("trinity.exchange", routingKey, event);
    }
}
