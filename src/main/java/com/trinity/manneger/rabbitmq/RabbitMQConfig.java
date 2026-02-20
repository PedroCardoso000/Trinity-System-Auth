package com.trinity.manneger.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ALUNO_QUEUE = "aluno.created.queue";

    @Bean
    public Queue alunoQueue() {
        return new Queue(ALUNO_QUEUE);
    }
}
