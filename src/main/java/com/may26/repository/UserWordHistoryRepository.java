package com.may26.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.may26.entity.DailyWord;
import com.may26.entity.User;
import com.may26.entity.UserWordHistory;

public interface UserWordHistoryRepository
        extends JpaRepository<UserWordHistory, Long> {

    boolean existsByUserAndWord(User user, DailyWord word);

    List<UserWordHistory> findByUser(User user);

    List<UserWordHistory> findByUserAndShownDate(
            User user,
            java.time.LocalDate shownDate
    );
}