package com.may26.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.may26.dto.DictionaryWordResponse;

@Service
public class DictionaryApiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private final Map<String, DictionaryWordResponse> fallbackWords;

    public DictionaryApiService() {

        this.restClient = RestClient
                .builder()
                .baseUrl("https://api.dictionaryapi.dev")
                .build();

        this.objectMapper = new ObjectMapper();

        this.fallbackWords = createFallbackWords();
    }

    public DictionaryWordResponse getWord(String word) {

        if (word == null || word.isBlank()) {
            return null;
        }

        String cleanWord = word.trim().toLowerCase();

        /*
         * First try the external Dictionary API.
         */
        try {

            String json = restClient
                    .get()
                    .uri("/api/v2/entries/en/{word}", cleanWord)
                    .retrieve()
                    .body(String.class);

            if (json != null && !json.isBlank()) {

                JsonNode root =
                        objectMapper.readTree(json);

                if (root.isArray() && !root.isEmpty()) {

                    JsonNode firstEntry = root.get(0);

                    String actualWord =
                            firstEntry
                                    .path("word")
                                    .asText("");

                    String meaning = "";
                    String example = "";

                    JsonNode meanings =
                            firstEntry.path("meanings");

                    if (meanings.isArray()) {

                        for (JsonNode meaningNode : meanings) {

                            JsonNode definitions =
                                    meaningNode.path("definitions");

                            if (!definitions.isArray()) {
                                continue;
                            }

                            for (JsonNode definition : definitions) {

                                if (meaning.isEmpty()) {

                                    meaning = definition
                                            .path("definition")
                                            .asText("");
                                }

                                if (example.isEmpty()) {

                                    example = definition
                                            .path("example")
                                            .asText("");
                                }

                                if (!meaning.isEmpty()
                                        && !example.isEmpty()) {
                                    break;
                                }
                            }

                            if (!meaning.isEmpty()
                                    && !example.isEmpty()) {
                                break;
                            }
                        }
                    }

                    /*
                     * Use API result only when all required
                     * Daily Word fields are available.
                     */
                    if (!actualWord.isBlank()
                            && !meaning.isBlank()
                            && !example.isBlank()) {

                        return new DictionaryWordResponse(
                                actualWord.trim(),
                                meaning.trim(),
                                example.trim()
                        );
                    }
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Dictionary API unavailable for '"
                            + cleanWord
                            + "': "
                            + e.getMessage()
            );
        }

        /*
         * External API failed or returned incomplete data.
         * Try the built-in fallback word bank.
         */
        DictionaryWordResponse fallback =
                fallbackWords.get(cleanWord);

        if (fallback != null) {

            System.out.println(
                    "Using built-in fallback for word: "
                            + cleanWord
            );

            return fallback;
        }

        /*
         * No API result and no fallback available.
         */
        System.out.println(
                "No dictionary data available for: "
                        + cleanWord
        );

        return null;
    }

    /*
     * Built-in fallback word bank.
     *
     * These words are used only when the external
     * Dictionary API is unavailable or incomplete.
     */
    private Map<String, DictionaryWordResponse> createFallbackWords() {

        Map<String, DictionaryWordResponse> words =
                new HashMap<>();

        words.put(
                "breathing",
                new DictionaryWordResponse(
                        "breathing",
                        "The act of taking air into and expelling it from the lungs.",
                        "Deep breathing can help you feel calm."
                )
        );

        words.put(
                "rapport",
                new DictionaryWordResponse(
                        "rapport",
                        "A friendly and understanding relationship between people.",
                        "She quickly built a good rapport with her teammates."
                )
        );

        words.put(
                "clarify",
                new DictionaryWordResponse(
                        "clarify",
                        "To make something easier to understand by explaining it clearly.",
                        "Could you clarify your answer during the meeting?"
                )
        );

        words.put(
                "reliable",
                new DictionaryWordResponse(
                        "reliable",
                        "Able to be trusted to work well or behave as expected.",
                        "She is a reliable person who always completes her work."
                )
        );

        words.put(
                "confident",
                new DictionaryWordResponse(
                        "confident",
                        "Feeling sure about your abilities or decisions.",
                        "He felt confident before giving his presentation."
                )
        );

        words.put(
                "efficient",
                new DictionaryWordResponse(
                        "efficient",
                        "Working well without wasting time, energy, or resources.",
                        "The new system makes the process more efficient."
                )
        );

        words.put(
                "adapt",
                new DictionaryWordResponse(
                        "adapt",
                        "To change or adjust to a new situation or environment.",
                        "Good employees can adapt to changing requirements."
                )
        );

        words.put(
                "communicate",
                new DictionaryWordResponse(
                        "communicate",
                        "To share information, ideas, or feelings with another person.",
                        "Good developers communicate clearly with their team."
                )
        );

        words.put(
                "collaborate",
                new DictionaryWordResponse(
                        "collaborate",
                        "To work together with others to achieve a common goal.",
                        "The developers collaborate on the new feature."
                )
        );

        words.put(
                "improve",
                new DictionaryWordResponse(
                        "improve",
                        "To make something better than it was before.",
                        "She practices every day to improve her English."
                )
        );

        words.put(
                "curious",
                new DictionaryWordResponse(
                        "curious",
                        "Eager to learn, know, or discover something.",
                        "A curious developer often explores new technologies."
                )
        );

        words.put(
                "precise",
                new DictionaryWordResponse(
                        "precise",
                        "Exact, accurate, and clearly defined.",
                        "Please provide precise instructions for the task."
                )
        );

        words.put(
                "creative",
                new DictionaryWordResponse(
                        "creative",
                        "Having the ability to produce new and original ideas.",
                        "She found a creative solution to the problem."
                )
        );

        words.put(
                "persistent",
                new DictionaryWordResponse(
                        "persistent",
                        "Continuing firmly despite difficulty or opposition.",
                        "Her persistent effort helped her solve the problem."
                )
        );

        words.put(
                "responsible",
                new DictionaryWordResponse(
                        "responsible",
                        "Having a duty to deal with something carefully and reliably.",
                        "He is responsible for completing the project on time."
                )
        );

        words.put(
                "flexible",
                new DictionaryWordResponse(
                        "flexible",
                        "Able to change or adjust easily to different circumstances.",
                        "A flexible approach can help when requirements change."
                )
        );

        words.put(
                "professional",
                new DictionaryWordResponse(
                        "professional",
                        "Showing the qualities and behavior expected in a skilled workplace.",
                        "She maintained a professional attitude during the interview."
                )
        );

        words.put(
                "initiative",
                new DictionaryWordResponse(
                        "initiative",
                        "The ability to act independently and start something without being asked.",
                        "He showed initiative by improving the existing process."
                )
        );

        words.put(
                "productive",
                new DictionaryWordResponse(
                        "productive",
                        "Producing useful results or achieving a significant amount of work.",
                        "The team had a productive discussion about the project."
                )
        );

        words.put(
                "consistent",
                new DictionaryWordResponse(
                        "consistent",
                        "Behaving or performing in the same reliable way over time.",
                        "Consistent practice is important when learning a new skill."
                )
        );

        return words;
    }
}