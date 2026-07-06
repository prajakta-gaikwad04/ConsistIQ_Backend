package com.may26.task.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.may26.entity.User;
import com.may26.repository.UserRepository;
import com.may26.task.dto.ChartPointDTO;
import com.may26.task.dto.StreakDto;
import com.may26.task.dto.UserStatsDto;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskStatus;
import com.may26.task.repository.TaskRepository;

@Service
public class AnalyticsService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // -------------------------------
    // 📊 BASIC DASHBOARD STATS
    // -------------------------------
    public UserStatsDto getUserStats(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks = taskRepository.findByUser(user);

        long total = tasks.size();

        long completed = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();

        long planned = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PLANNED)
                .count();
        long overdue = tasks.stream()
                .filter(t -> t.getDueDate() != null
                        && t.getDueDate().isBefore(LocalDate.now())
                        && t.getStatus() != TaskStatus.COMPLETED
                        && t.getStatus() != TaskStatus.CANCELLED)
                .count();

        double completionRate = total == 0 ? 0 : (completed * 100.0 / total);

        UserStatsDto dto = new UserStatsDto();
        dto.setTotalTasks(total);
        dto.setCompletedTasks(completed);
        dto.setPlannedTasks(planned);
        dto.setOverdueTasks(overdue);
        dto.setCompletionRate(completionRate);

        return dto;
    }

    // -------------------------------
    // 📊 WEEKLY CHART (LAST 7 DAYS)
    // -------------------------------
    public List<ChartPointDTO> getWeeklyChart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);

        List<Task> tasks = taskRepository.findByUser(user);

        Map<LocalDate, Long> grouped = tasks.stream()
                .filter(t -> t.getCompletedAt() != null)
                .filter(t -> !t.getCompletedAt().isBefore(start))
                .filter(t -> !t.getCompletedAt().isAfter(end))
                .collect(Collectors.groupingBy(
                        Task::getCompletedAt,
                        Collectors.counting()
                ));

        List<ChartPointDTO> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            LocalDate date = start.plusDays(i);
            String label = date.getDayOfWeek().toString().substring(0, 3);

            result.add(new ChartPointDTO(
                    label,
                    grouped.getOrDefault(date, 0L)
            ));
        }

        return result;
    }

    // -------------------------------
    // 📊 MONTHLY CHART (LAST 30 DAYS)
    // -------------------------------
    public List<ChartPointDTO> getMonthlyChart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(29);

        List<Task> tasks = taskRepository.findByUser(user);

        Map<LocalDate, Long> grouped = tasks.stream()
                .filter(t -> t.getCompletedAt() != null)
                .filter(t -> !t.getCompletedAt().isBefore(start))
                .filter(t -> !t.getCompletedAt().isAfter(end))
                .collect(Collectors.groupingBy(
                        Task::getCompletedAt,
                        Collectors.counting()
                ));

        List<ChartPointDTO> result = new ArrayList<>();

        for (int i = 0; i < 30; i++) {

            LocalDate date = start.plusDays(i);

            result.add(new ChartPointDTO(
                    date.toString().substring(5), // MM-DD format
                    grouped.getOrDefault(date, 0L)
            ));
        }

        return result;
    }

    // -------------------------------
    // 🔥 STREAK BASE DATA (OPTIONAL)
    // -------------------------------
    public List<LocalDate> getCompletionDates(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUser(user)
                .stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .map(Task::getCompletedAt)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
    }
    
    
    public StreakDto getStreak(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks = taskRepository.findByUser(user);

        // Get all unique completion dates
        Set<LocalDate> dates = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .map(Task::getCompletedAt)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (dates.isEmpty()) {
            return new StreakDto(0, 0);
        }

        // Sort dates
        List<LocalDate> sorted = new ArrayList<>(dates);
        sorted.sort(Comparator.naturalOrder());

        int bestStreak = 0;
        int currentStreak = 0;
        int tempStreak = 1;

        // Find BEST streak
        for (int i = 1; i < sorted.size(); i++) {

            LocalDate prev = sorted.get(i - 1);
            LocalDate curr = sorted.get(i);

            if (prev.plusDays(1).equals(curr)) {
                tempStreak++;
            } else {
                bestStreak = Math.max(bestStreak, tempStreak);
                tempStreak = 1;
            }
        }

        bestStreak = Math.max(bestStreak, tempStreak);

        // Find CURRENT streak (from today backwards)
        LocalDate today = LocalDate.now();
        currentStreak = 0;

        while (dates.contains(today.minusDays(currentStreak))) {
            currentStreak++;
        }

        return new StreakDto(currentStreak, bestStreak);
    }
}