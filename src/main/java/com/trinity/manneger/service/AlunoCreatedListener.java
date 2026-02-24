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
            // ADICIONAR UM MENSAGEM DE ERROR;
            System.out.println("Erro: Aluno com email " + event.getEmail() + " já existe.");
            return;
        }

        User user = new User();
        user.setName(event.getNome());
        user.setEmail(event.getEmail());
        user.setPassword(""); // ainda não tem senha
        user.setRole(Role.STUDENT);
        user.setActive(false);
        user.setIdAcademic(event.getAcademicId());
        user.setIdBranch(event.getBranchId());

        userRepository.save(user);
    }
}
