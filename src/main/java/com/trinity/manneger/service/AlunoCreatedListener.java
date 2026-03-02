package com.trinity.manneger.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.AlunoCreatedEvent;
import com.trinity.manneger.domain.dto.AlunoDeletedEvent;
import com.trinity.manneger.domain.dto.AlunoUpdatedEvent;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.rabbitmq.RabbitMQConfig;
import com.trinity.manneger.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoCreatedListener {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @RabbitListener(queues = RabbitMQConfig.ALUNO_QUEUE)
    public void handleAlunoCreated(AlunoCreatedEvent event) {

        if (userRepository.existsByEmail(event.getEmail())) {
            // ADICIONAR UM MENSAGEM DE ERROR;
            System.out.println("Erro: Aluno with email " + event.getEmail() + " already exists.");
            return;
        }

        User user = new User();
        user.setName(event.getNome());
        user.setEmail(event.getEmail());
        user.setPassword(""); 
        user.setRole(Role.STUDENT);
        user.setActive(false);
        user.setIdAcademic(event.getAcademicId());
        if (event.getBranchId() != null) {
            user.setIdBranch(List.of(event.getBranchId()));
        }

        userRepository.save(user);

        userEventPublisher.publishUserCreated(user.getId(), user.getEmail());
    }

    @RabbitListener(queues = RabbitMQConfig.ALUNO_UPDATED_QUEUE)
    public void handleAlunoUpdated(AlunoUpdatedEvent event) {

        userRepository.findByEmail(event.getEmail())
                .ifPresent(user -> {
                    user.setName(event.getNome());
                    user.setEmail(event.getEmail());
                    user.setActive(event.getAtivo());
                    userRepository.save(user);
                });
    }

    @RabbitListener(queues = RabbitMQConfig.ALUNO_DELETED_QUEUE)
    public void handleAlunoDeleted(AlunoDeletedEvent event) {

        userRepository.findByEmail(event.getEmail())
                .ifPresent(user -> {
                    userRepository.delete(user);
                });
    }
}
