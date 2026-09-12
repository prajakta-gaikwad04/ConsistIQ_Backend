package com.may26.task.dto;

import java.util.List;

public class DailySummaryDTO {

    private List<TaskResponseDto> overdueTasks;
    private List<TaskResponseDto> todayTasks;
    private List<TaskResponseDto> highPriorityTasks;
    private List<TaskResponseDto> completedTodayTasks;
    private List<TaskResponseDto> tomorrowTasks;

    public DailySummaryDTO(
            List<TaskResponseDto> overdueTasks,
            List<TaskResponseDto> todayTasks,
            List<TaskResponseDto> highPriorityTasks,
            List<TaskResponseDto> completedTodayTasks,
            List<TaskResponseDto> tomorrowTasks) {

        this.overdueTasks = overdueTasks;
        this.todayTasks = todayTasks;
        this.highPriorityTasks = highPriorityTasks;
        this.completedTodayTasks = completedTodayTasks;
        this.tomorrowTasks = tomorrowTasks;
    }

    public List<TaskResponseDto> getOverdueTasks() {
        return overdueTasks;
    }

    public List<TaskResponseDto> getTodayTasks() {
        return todayTasks;
    }

    public List<TaskResponseDto> getHighPriorityTasks() {
        return highPriorityTasks;
    }

    public List<TaskResponseDto> getCompletedTodayTasks() {
        return completedTodayTasks;
    }

    public List<TaskResponseDto> getTomorrowTasks() {
        return tomorrowTasks;
    }
}