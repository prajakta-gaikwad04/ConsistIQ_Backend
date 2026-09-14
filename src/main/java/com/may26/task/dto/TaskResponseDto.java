package com.may26.task.dto;

import java.time.LocalDate;
import java.util.List;

import com.may26.task.enums.RecurrenceType;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;

public class TaskResponseDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private String category;
    private boolean recurring;

    private RecurrenceType recurrenceType;

    private LocalDate recurrenceEndDate;
    private List<TaskAttachmentResponseDto> attachments;
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

	public boolean isRecurring() {
	    return recurring;
	}

	public void setRecurring(boolean recurring) {
	    this.recurring = recurring;
	}

	public RecurrenceType getRecurrenceType() {
	    return recurrenceType;
	}

	public void setRecurrenceType(RecurrenceType recurrenceType) {
	    this.recurrenceType = recurrenceType;
	}

	public LocalDate getRecurrenceEndDate() {
	    return recurrenceEndDate;
	}

	public void setRecurrenceEndDate(LocalDate recurrenceEndDate) {
	    this.recurrenceEndDate = recurrenceEndDate;
	}
	public List<TaskAttachmentResponseDto> getAttachments() {
	    return attachments;
	}

	public void setAttachments(List<TaskAttachmentResponseDto> attachments) {
	    this.attachments = attachments;
	}
}