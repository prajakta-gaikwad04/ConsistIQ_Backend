package com.may26.task.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.may26.task.dto.ChartPointDTO;
import com.may26.task.dto.StreakDto;
import com.may26.task.dto.UserStatsDto;
import com.may26.task.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

 
    @GetMapping("/stats")
    public UserStatsDto getStats(Principal principal) {

        return analyticsService.getUserStats(principal.getName());
    }

   
    @GetMapping("/weekly-chart")
    public List<ChartPointDTO> getWeeklyChart(Principal principal) {

        return analyticsService.getWeeklyChart(principal.getName());
    }

   
    @GetMapping("/monthly-chart")
    public List<ChartPointDTO> getMonthlyChart(Principal principal) {

        return analyticsService.getMonthlyChart(principal.getName());
    }


    @GetMapping("/completion-dates")
    public List<LocalDate> getCompletionDates(
            Principal principal,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return analyticsService.getCompletionDates(
                principal.getName(),
                startDate,
                endDate
        );
    }
    
    @GetMapping("/streak")
    public StreakDto getStreak(Principal principal) {

        return analyticsService.getStreak(principal.getName());
    }
    
}