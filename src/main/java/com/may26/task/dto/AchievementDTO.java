package com.may26.task.dto;
public class AchievementDTO {

    private String title;
    private String icon;
    private boolean unlocked;

    public AchievementDTO(
            String title,
            String icon,
            boolean unlocked) {

        this.title = title;
        this.icon = icon;
        this.unlocked = unlocked;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isUnlocked() {
        return unlocked;
    }
}