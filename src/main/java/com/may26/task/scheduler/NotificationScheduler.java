package com.may26.task.scheduler;

import com.may26.entity.User;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskStatus;
import com.may26.task.repository.NotificationRepository;
import com.may26.task.repository.TaskRepository;
import com.may26.task.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 9 * * *") // Every day at 9 AM
    public void generateReminders() {

        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {

            if (task.getStatus() == TaskStatus.COMPLETED) {
                continue;
            }

            LocalDate due = task.getDueDate();
            LocalDate today = LocalDate.now();

            if (due == null) {
                continue;
            }

            String message = null;

            if (due.equals(today)) {
                message = "🔔 Task is due today: " + task.getTitle();
            }
            else if (due.isBefore(today)) {
                message = "⚠️ Task is overdue: " + task.getTitle();
            }

            if (message != null) {

                User user = task.getUser();

                boolean exists =
                        notificationRepository.existsByUserAndTaskAndMessage(
                                user,
                                task,
                                message
                        );

                if (!exists) {
                    notificationService.createNotification(
                            user,
                            task,
                            message
                    );
                }
            }
        }
    }
}