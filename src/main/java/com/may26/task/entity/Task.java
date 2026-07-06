package com.may26.task.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.may26.entity.User;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private Long id;
	
	@NotBlank(message="Title cannot be empty")
	private String title;
	
	@NotBlank(message="Description cannot be empty")
	private String description;
	
	@NotNull(message="Status is required")
	private TaskStatus status;
	
	@NotNull(message="Priority is required")
	private TaskPriority priority;
	
	@NotNull(message="Due Date is required")
	private LocalDate dueDate;
	
	@NotNull(message="Category cannot be empty")
	private String category;
	
	private String attachmentPath;
	
	private LocalDate completedAt;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user;
			
	public Task() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public TaskPriority getPriority() {
		return priority;
	}
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public LocalDate getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(LocalDate completedAt) {
		this.completedAt = completedAt;
	}
	
	

}
