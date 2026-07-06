package com.may26.task.dto;

public class UserStatsDto {

    private long totalTasks;
    private long completedTasks;
    private long plannedTasks;
    private long overdueTasks;
    private double completionRate;

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getPlannedTasks() {
        return plannedTasks;
    }

    public void setPlannedTasks(long pendingTasks) {
        this.plannedTasks = pendingTasks;
    }

    public long getOverdueTasks() {
        return overdueTasks;
    }

    public void setOverdueTasks(long overdueTasks) {
        this.overdueTasks = overdueTasks;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
}