package com.may26.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.may26.dto.DictionaryWordResponse;
import com.may26.entity.DailyWord;
import com.may26.repository.DailyWordRepository;

@Service
public class DailyWordReplenishmentService {

    private static final long MIN_WORD_POOL = 10;

    private final WordDiscoveryService wordDiscoveryService;
    private final WordValidationService wordValidationService;
    private final DailyWordRepository dailyWordRepository;

    public DailyWordReplenishmentService(
            WordDiscoveryService wordDiscoveryService,
            WordValidationService wordValidationService,
            DailyWordRepository dailyWordRepository) {

        this.wordDiscoveryService = wordDiscoveryService;
        this.wordValidationService = wordValidationService;
        this.dailyWordRepository = dailyWordRepository;
    }

    public void replenishWords() {

        try {

            long currentWordCount =
                    dailyWordRepository.count();

            System.out.println(
                    "Current daily word pool: "
                            + currentWordCount
            );

            if (currentWordCount >= MIN_WORD_POOL) {

                System.out.println(
                        "Daily word pool is sufficient. "
                                + "No replenishment needed."
                );

                return;
            }

            System.out.println(
                    "Daily word pool is low. "
                            + "Starting replenishment..."
            );

            List<String> candidates =
                    wordDiscoveryService.findWords();

            if (candidates == null
                    || candidates.isEmpty()) {

                System.out.println(
                        "No new word candidates found."
                );

                return;
            }

            for (String candidate : candidates) {

                try {

                    DictionaryWordResponse result =
                            wordValidationService
                                    .validateWord(candidate);

                    if (result == null) {

                        System.out.println(
                                "Skipping invalid word: "
                                        + candidate
                        );

                        continue;
                    }

                    String word =
                            result.getWord()
                                    .trim();

                    if (dailyWordRepository
                            .findByWordIgnoreCase(word)
                            .isPresent()) {

                        System.out.println(
                                "Word already exists: "
                                        + word
                        );

                        continue;
                    }

                    DailyWord dailyWord =
                            new DailyWord(
                                    word,
                                    result.getMeaning(),
                                    result.getExample()
                            );

                    dailyWordRepository.save(
                            dailyWord
                    );

                    System.out.println(
                            "New word saved: "
                                    + word
                    );

                } catch (Exception e) {

                    System.err.println(
                            "Failed to process word '"
                                    + candidate
                                    + "': "
                                    + e.getMessage()
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Daily word replenishment failed: "
                            + e.getMessage()
            );
        }
    }
}