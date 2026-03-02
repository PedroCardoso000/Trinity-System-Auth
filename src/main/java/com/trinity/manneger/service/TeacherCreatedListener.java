package com.trinity.manneger.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.TeacherCreatedEvent;
import com.trinity.manneger.domain.dto.TeacherDeletedEvent;
import com.trinity.manneger.domain.dto.TeacherUpdatedEvent;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.rabbitmq.RabbitMQConfig;
import com.trinity.manneger.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherCreatedListener {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @RabbitListener(queues = RabbitMQConfig.TEACHER_QUEUE)
    public void handleTeacherCreated(TeacherCreatedEvent event) {

        if (userRepository.existsByEmail(event.getEmail())) {
            // ADICIONAR UM MENSAGEM DE ERROR;
            System.out.println("Erro: Professor with email " + event.getEmail() + " already exists.");
            return;
        }

        User user = new User();
        user.setName(event.getName());
        user.setEmail(event.getEmail());
        user.setPassword("");
        user.setRole(Role.TEACHER);
        user.setActive(false);
        user.setIdAcademic(event.getAcademicId());
        user.setIdBranch(event.getBranchId());

        userRepository.save(user);

        userEventPublisher.publishUserCreated(user.getId(), user.getEmail());
    }

    @RabbitListener(queues = RabbitMQConfig.TEACHER_UPDATED_QUEUE)
    public void handleTeacherUpdated(TeacherUpdatedEvent event) {

        userRepository.findByEmail(event.getEmail())
                .ifPresent(user -> {
                    user.setName(event.getName());
                    user.setActive(event.getActive());
                    userRepository.save(user);
                });
    }

    @RabbitListener(queues = RabbitMQConfig.TEACHER_DELETED_QUEUE)
    public void handleTeacherDeleted(TeacherDeletedEvent event) {

        userRepository.findByEmail(event.getEmail())
                .ifPresent(user -> {
                    user.setActive(false); // disable login
                    userRepository.save(user);
                });
    }
}
