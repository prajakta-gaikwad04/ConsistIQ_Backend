package com.may26.service;

import java.util.ArrayList;
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

            /*
             * First try words discovered from Datamuse.
             */
            List<String> candidates =
                    wordDiscoveryService.findWords();

            if (candidates == null) {
                candidates = new ArrayList<>();
            }

            /*
             * Add reliable built-in candidates as a fallback.
             * These are appended only when the existing pool
             * still needs more words.
             */
            List<String> fallbackCandidates = List.of(
                    "clarify",
                    "reliable",
                    "confident",
                    "efficient",
                    "adapt",
                    "communicate",
                    "collaborate",
                    "improve",
                    "curious",
                    "precise",
                    "creative",
                    "persistent",
                    "responsible",
                    "flexible",
                    "professional",
                    "initiative",
                    "productive",
                    "consistent",
                    "supportive",
                    "effective"
            );

            for (String fallbackWord : fallbackCandidates) {

                if (!candidates.contains(fallbackWord)) {
                    candidates.add(fallbackWord);
                }
            }

            /*
             * Process candidates until the pool reaches 10.
             */
            for (String candidate : candidates) {

                if (dailyWordRepository.count()
                        >= MIN_WORD_POOL) {
                    break;
                }

                try {

                    if (candidate == null
                            || candidate.isBlank()) {
                        continue;
                    }

                    String cleanCandidate =
                            candidate.trim().toLowerCase();

                    /*
                     * Skip words already present in the
                     * DailyWord table.
                     */
                    if (dailyWordRepository
                            .findByWordIgnoreCase(cleanCandidate)
                            .isPresent()) {

                        System.out.println(
                                "Word already exists: "
                                        + cleanCandidate
                        );

                        continue;
                    }

                    /*
                     * Try Dictionary API first.
                     * DictionaryApiService now has its
                     * built-in fallback.
                     */
                    DictionaryWordResponse result =
                            wordValidationService
                                    .validateWord(cleanCandidate);

                    if (result == null) {

                        System.out.println(
                                "Skipping invalid word: "
                                        + cleanCandidate
                        );

                        continue;
                    }

                    String word =
                            result.getWord()
                                    .trim();

                    /*
                     * Double-check before saving.
                     */
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

                    dailyWordRepository.save(dailyWord);

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

            System.out.println(
                    "Daily word pool after replenishment: "
                            + dailyWordRepository.count()
            );

        } catch (Exception e) {

            System.err.println(
                    "Daily word replenishment failed: "
                            + e.getMessage()
            );
        }
    }
}