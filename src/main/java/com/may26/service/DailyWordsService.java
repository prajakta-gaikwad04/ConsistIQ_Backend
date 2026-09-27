package com.may26.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private static final int DAILY_WORD_LIMIT = 5;

    private final DailyWordRepository dailyWordRepository;
    private final UserRepository userRepository;
    private final UserWordHistoryRepository userWordHistoryRepository;
    private final DailyWordReplenishmentService
            dailyWordReplenishmentService;

    public DailyWordsService(
            DailyWordRepository dailyWordRepository,
            UserRepository userRepository,
            UserWordHistoryRepository userWordHistoryRepository,
            DailyWordReplenishmentService dailyWordReplenishmentService) {

        this.dailyWordRepository = dailyWordRepository;
        this.userRepository = userRepository;
        this.userWordHistoryRepository =
                userWordHistoryRepository;
        this.dailyWordReplenishmentService =
                dailyWordReplenishmentService;
    }

    public List<DailyWord> getDailyWords() {

        try {

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getName() == null) {

                return Collections.emptyList();
            }

            String email =
                    authentication.getName();

            User user =
                    userRepository
                            .findByEmail(email)
                            .orElse(null);

            if (user == null) {

                return Collections.emptyList();
            }

            LocalDate today =
                    LocalDate.now();

            /*
             * STEP 1
             *
             * Check today's already assigned words.
             */
            List<UserWordHistory> todaysHistory =
                    userWordHistoryRepository
                            .findByUserAndShownDate(
                                    user,
                                    today
                            );

            /*
             * If today's 5 words already exist,
             * return exactly those 5.
             */
            if (todaysHistory.size()
                    >= DAILY_WORD_LIMIT) {

                return todaysHistory.stream()
                        .map(UserWordHistory::getWord)
                        .limit(DAILY_WORD_LIMIT)
                        .toList();
            }

            /*
             * STEP 2
             *
             * We need more words.
             */
            int remaining =
                    DAILY_WORD_LIMIT
                            - todaysHistory.size();

            System.out.println(
                    "Today's words: "
                            + todaysHistory.size()
                            + ", remaining: "
                            + remaining
            );

            /*
             * STEP 3
             *
             * Replenish the global pool BEFORE
             * looking for unseen words.
             */
            dailyWordReplenishmentService
                    .replenishWords();

            /*
             * STEP 4
             *
             * Find words this user has NEVER seen.
             */
            List<DailyWord> availableWords =
                    dailyWordRepository
                            .findWordsNotSeenByUser(user);

            /*
             * Select only the number we need.
             */
            List<DailyWord> newWords =
                    availableWords.stream()
                            .limit(remaining)
                            .toList();

            /*
             * STEP 5
             *
             * Save the new words in today's history.
             */
            for (DailyWord word : newWords) {

                UserWordHistory history =
                        new UserWordHistory(
                                user,
                                word,
                                today
                        );

                userWordHistoryRepository
                        .save(history);
            }

            /*
             * STEP 6
             *
             * Combine old + new.
             */
            List<DailyWord> result =
                    new ArrayList<>();

            result.addAll(
                    todaysHistory.stream()
                            .map(UserWordHistory::getWord)
                            .toList()
            );

            result.addAll(newWords);

            /*
             * Return maximum 5 words.
             */
            return result.stream()
                    .limit(DAILY_WORD_LIMIT)
                    .toList();

        } catch (Exception e) {

            System.err.println(
                    "Daily Words unavailable: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }
}