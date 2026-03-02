package com.trinity.manneger.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String ALUNO_QUEUE = "aluno.created.queue";
    public static final String ALUNO_UPDATED_QUEUE = "aluno.updated.queue";
    public static final String ALUNO_DELETED_QUEUE = "aluno.deleted.queue";

    public static final String TEACHER_QUEUE = "teacher.created.queue";
    public static final String TEACHER_UPDATED_QUEUE = "teacher.updated.queue";
    public static final String TEACHER_DELETED_QUEUE = "teacher.deleted.queue";

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_ROUTING_KEY = "user.created";

    @Bean
    public Queue alunoQueue() {
        return new Queue(ALUNO_QUEUE);
    }

    @Bean
    public Queue teacherQueue() {
        return new Queue(TEACHER_QUEUE);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue teacherUpdatedQueue() {
        return new Queue(TEACHER_UPDATED_QUEUE);
    }

    @Bean
    public Queue teacherDeletedQueue() {
        return new Queue(TEACHER_DELETED_QUEUE);
    }

    @Bean
    public Binding teacherUpdatedBinding(Queue teacherUpdatedQueue, TopicExchange teacherExchange) {
        return BindingBuilder
                .bind(teacherUpdatedQueue)
                .to(teacherExchange)
                .with("teacher.updated");
    }

    @Bean
    public Binding teacherDeletedBinding(Queue teacherDeletedQueue, TopicExchange teacherExchange) {
        return BindingBuilder
                .bind(teacherDeletedQueue)
                .to(teacherExchange)
                .with("teacher.deleted");
    }

    @Bean
    public Queue alunoUpdatedQueue() {
        return new Queue(ALUNO_UPDATED_QUEUE);
    }

    @Bean
    public Queue alunoDeletedQueue() {
        return new Queue(ALUNO_DELETED_QUEUE);
    }

    @Bean
    public Binding alunoUpdatedBinding(Queue alunoUpdatedQueue, TopicExchange alunoExchange) {
        return BindingBuilder
                .bind(alunoUpdatedQueue)
                .to(alunoExchange)
                .with("aluno.updated");
    }

    @Bean
    public Binding alunoDeletedBinding(Queue alunoDeletedQueue, TopicExchange alunoExchange) {
        return BindingBuilder
                .bind(alunoDeletedQueue)
                .to(alunoExchange)
                .with("aluno.deleted");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
