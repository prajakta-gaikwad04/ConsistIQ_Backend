package com.may26.task.dto;

import java.time.LocalDate;

import com.may26.task.enums.RecurrenceType;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @NotNull(message = "Due Date is required")
    private LocalDate dueDate;

    @NotBlank(message = "Category cannot be empty")
    private String category;
    private boolean recurring;

    private RecurrenceType recurrenceType;

    private LocalDate recurrenceEndDate;
    public boolean isRecurring() {
        return recurring;
    }
	public TaskRequestDto() {
		super();
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

}