package com.may26.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.may26.entity.User;
import com.may26.task.entity.Notification;
import com.may26.task.entity.Task;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);
    
    boolean existsByUserAndTaskAndMessage(User user, Task task, String message);
    }