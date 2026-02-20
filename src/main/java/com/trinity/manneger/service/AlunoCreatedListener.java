package com.trinity.manneger.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.AlunoCreatedEvent;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.rabbitmq.RabbitMQConfig;
import com.trinity.manneger.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoCreatedListener {

    private final UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.ALUNO_QUEUE)
    public void handleAlunoCreated(AlunoCreatedEvent event) {

        if (userRepository.existsByEmail(event.getEmail())) {
            return;
        }

        User user = User.builder()
                .name(event.getNome())
                .email(event.getEmail())
                .password("") // ainda não tem senha
                .role(Role.USER)
                .active(false)
                .build();

        userRepository.save(user);
    }
}
