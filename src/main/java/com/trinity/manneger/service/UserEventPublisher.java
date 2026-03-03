package com.trinity.manneger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.dto.UserCreatedEvent;
import com.trinity.manneger.rabbitmq.RabbitMQConfig;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserCreated(Long userId, String email) {

        UserCreatedEvent event = new UserCreatedEvent(userId, email);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_ROUTING_KEY,
                event);
    }
}
