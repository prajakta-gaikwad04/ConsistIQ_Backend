package com.may26.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.may26.entity.DailyWord;
import com.may26.entity.User;
import com.may26.entity.UserWordHistory;
import com.may26.repository.DailyWordRepository;
import com.may26.repository.UserRepository;
import com.may26.repository.UserWordHistoryRepository;

@Service
public class DailyWordsService {

    private final DailyWordRepository dailyWordRepository;
    private final UserRepository userRepository;
    private final UserWordHistoryRepository userWordHistoryRepository;

    public DailyWordsService(
            DailyWordRepository dailyWordRepository,
            UserRepository userRepository,
            UserWordHistoryRepository userWordHistoryRepository) {

        this.dailyWordRepository = dailyWordRepository;
        this.userRepository = userRepository;
        this.userWordHistoryRepository = userWordHistoryRepository;
    }

    public List<DailyWord> getDailyWords() {

        try {

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null ||
                !authentication.isAuthenticated()) {

                return Collections.emptyList();
            }

            String email = authentication.getName();

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            if (user == null) {
                return Collections.emptyList();
            }

            LocalDate today = LocalDate.now();

            /*
             * IMPORTANT:
             * If today's words were already created,
             * return the same words instead of generating
             * another set after every refresh.
             */
            List<UserWordHistory> todaysHistory =
                    userWordHistoryRepository
                            .findByUserAndShownDate(user, today);

            if (!todaysHistory.isEmpty()) {

                return todaysHistory.stream()
                        .map(UserWordHistory::getWord)
                        .limit(5)
                        .toList();
            }

            /*
             * No words have been assigned to this user today.
             * Find words this user has never seen before.
             */
            List<DailyWord> availableWords =
                    dailyWordRepository
                            .findWordsNotSeenByUser(user);

            if (availableWords.isEmpty()) {
                return Collections.emptyList();
            }

            /*
             * Take a maximum of 5 new words.
             */
            List<DailyWord> todaysWords =
                    availableWords.stream()
                            .limit(5)
                            .toList();

            /*
             * Save today's words into user history.
             */
            for (DailyWord word : todaysWords) {

                UserWordHistory history =
                        new UserWordHistory(
                                user,
                                word,
                                today
                        );

                userWordHistoryRepository.save(history);
            }

            return todaysWords;

        } catch (Exception e) {

            System.err.println(
                    "Daily Words unavailable: "
                    + e.getMessage()
            );

            return Collections.emptyList();
        }
    }
}