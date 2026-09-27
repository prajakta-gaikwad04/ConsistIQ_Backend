package com.may26.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.may26.dto.DictionaryWordResponse;
import com.may26.entity.DailyWord;
import com.may26.repository.DailyWordRepository;

@Service
public class DailyWordReplenishmentService {

    /*
     * Keep a reasonably large pool.
     *
     * 10 is too small because users are not allowed
     * to see the same word again.
     */
    private static final long MIN_WORD_POOL = 50;

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

            long currentCount =
                    dailyWordRepository.count();

            System.out.println(
                    "Current daily word pool: "
                            + currentCount
            );

            if (currentCount >= MIN_WORD_POOL) {

                System.out.println(
                        "Daily word pool is sufficient."
                );

                return;
            }

            /*
             * IMPORTANT:
             * Always create a mutable ArrayList.
             *
             * WordDiscoveryService may return an immutable
             * List.of() when Datamuse is unavailable.
             */
            List<String> candidates =
                    new ArrayList<>(
                            wordDiscoveryService.findWords()
                    );

            /*
             * Reliable fallback words.
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
                    "effective",
                    "organized",
                    "focused",
                    "respectful",
                    "accurate",
                    "valuable",
                    "practical",
                    "positive",
                    "capable",
                    "independent",
                    "motivated",
                    "dedicated",
                    "successful",
                    "constructive",
                    "innovative",
                    "logical",
                    "relevant",
                    "appropriate",
                    "essential",
                    "specific",
                    "accurate",
                    "creative",
                    "strategic",
                    "flexible",
                    "professional",
                    "solution",
                    "priority",
                    "progress",
                    "quality",
                    "resourceful",
                    "productive",
                    "confident"
            );

            /*
             * Add fallback words to discovered words.
             */
            for (String fallbackWord : fallbackCandidates) {

                if (!candidates.contains(fallbackWord)) {

                    candidates.add(fallbackWord);
                }
            }

            /*
             * Process candidates until pool reaches 50.
             */
            for (String candidate : candidates) {

                if (dailyWordRepository.count()
                        >= MIN_WORD_POOL) {

                    break;
                }

                if (candidate == null
                        || candidate.isBlank()) {

                    continue;
                }

                String cleanCandidate =
                        candidate.trim().toLowerCase();

                try {

                    /*
                     * Don't insert duplicates.
                     */
                    if (dailyWordRepository
                            .findByWordIgnoreCase(
                                    cleanCandidate)
                            .isPresent()) {

                        continue;
                    }

                    /*
                     * Get proper meaning + example
                     * from your Dictionary API.
                     */
                    DictionaryWordResponse result =
                            wordValidationService
                                    .validateWord(
                                            cleanCandidate
                                    );

                    if (result == null) {

                        System.out.println(
                                "Dictionary validation failed: "
                                        + cleanCandidate
                        );

                        continue;
                    }

                    if (result.getWord() == null
                            || result.getWord().isBlank()
                            || result.getMeaning() == null
                            || result.getMeaning().isBlank()
                            || result.getExample() == null
                            || result.getExample().isBlank()) {

                        continue;
                    }

                    String finalWord =
                            result.getWord()
                                    .trim()
                                    .toLowerCase();

                    /*
                     * Check again before saving.
                     */
                    if (dailyWordRepository
                            .findByWordIgnoreCase(finalWord)
                            .isPresent()) {

                        continue;
                    }

                    DailyWord dailyWord =
                            new DailyWord(
                                    finalWord,
                                    result.getMeaning(),
                                    result.getExample()
                            );

                    dailyWordRepository.save(dailyWord);

                    System.out.println(
                            "New daily word saved: "
                                    + finalWord
                    );

                } catch (Exception e) {

                    System.err.println(
                            "Failed to process word "
                                    + cleanCandidate
                                    + ": "
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