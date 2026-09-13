package com.may26.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyWordReplenishmentScheduler {

    private final DailyWordReplenishmentService
            dailyWordReplenishmentService;

    public DailyWordReplenishmentScheduler(
            DailyWordReplenishmentService dailyWordReplenishmentService) {

        this.dailyWordReplenishmentService =
                dailyWordReplenishmentService;
    }

    @Scheduled(
            fixedDelay = 3600000,
            initialDelay = 60000
    )
    public void checkAndReplenishWords() {

        System.out.println(
                "Daily word scheduler: checking word pool..."
        );

        dailyWordReplenishmentService.replenishWords();
    }
}