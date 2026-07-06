package com.may26.task.dto;

import java.time.LocalDate;

public class StreakCalendarDTO {

    private LocalDate date;
    private boolean completed;

    public StreakCalendarDTO() {
    }

    public StreakCalendarDTO(
            LocalDate date,
            boolean completed
    ) {
        this.date = date;
        this.completed = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}