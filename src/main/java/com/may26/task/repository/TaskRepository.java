package com.may26.task.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.may26.entity.User;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;



public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task>{
	List<Task> findByUser(User user);
	List<Task> findByUserAndStatus(User user,TaskStatus status);
	List<Task> findByUserAndPriority(User user,TaskPriority priority);
	List<Task> findByUserAndCategory(User user,String category);
	
	Page<Task> findByUser(User user, Pageable pageable);
	
	List<Task> findByUserAndTitleContainingIgnoreCase(User user,String keyword);
	
	long countByUser(User user);
	
	long countByUserAndStatus(User user,TaskStatus status);
	
	List<Task> findByUserAndStatusAndPriorityAndCategory(
	        User user,
	        TaskStatus status,
	        TaskPriority priority,
	        String category);
	
	List<Task> findByUserAndDueDate(User user, LocalDate dueDate);
	
	
	List<Task> findByUserAndDueDateBefore(User user, LocalDate dueDate);
	
	List<Task> findByUserAndCompletedAt(User user, LocalDate date);
	
	
	long countByUserAndCompletedAt(
		    User user,
		    LocalDate completedAt
		);
	
	
	List<Task> findByUserAndDueDateBetween(
	        User user,
	        LocalDate startDate,
	        LocalDate endDate
	);
}

