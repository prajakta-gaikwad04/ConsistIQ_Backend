package com.may26.task.dto;

public class StreakDto {

    private int currentStreak;
    private int bestStreak;

    public StreakDto(int currentStreak, int bestStreak) {
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }
}