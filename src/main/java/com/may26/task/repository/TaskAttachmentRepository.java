package com.may26.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.may26.task.entity.Task;
import com.may26.task.entity.TaskAttachment;

public interface TaskAttachmentRepository
        extends JpaRepository<TaskAttachment, Long> {

    List<TaskAttachment> findByTask(Task task);
}