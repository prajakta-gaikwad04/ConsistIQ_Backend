package com.may26.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.may26.dto.DictionaryWordResponse;

@Service
public class DictionaryApiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public DictionaryApiService() {

        this.restClient = RestClient
                .builder()
                .baseUrl("https://api.dictionaryapi.dev")
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public DictionaryWordResponse getWord(String word) {

        try {

            String json = restClient
                    .get()
                    .uri("/api/v2/entries/en/{word}", word)
                    .retrieve()
                    .body(String.class);

            if (json == null || json.isBlank()) {
                return null;
            }

            JsonNode root =
                    objectMapper.readTree(json);

            if (!root.isArray() || root.isEmpty()) {
                return null;
            }

            JsonNode firstEntry = root.get(0);

            String actualWord =
                    firstEntry
                            .path("word")
                            .asText("");

            String meaning = "";
            String example = "";

            JsonNode meanings =
                    firstEntry.path("meanings");

            if (!meanings.isArray()) {
                return null;
            }

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

            // We need all three fields for our Daily Words feature.
            if (actualWord.isBlank()
                    || meaning.isBlank()
                    || example.isBlank()) {

                System.out.println(
                        "Dictionary data incomplete for: "
                                + word
                );

                return null;
            }

            return new DictionaryWordResponse(
                    actualWord,
                    meaning,
                    example
            );

        } catch (Exception e) {

            System.err.println(
                    "Dictionary API unavailable for '"
                            + word
                            + "': "
                            + e.getMessage()
            );

            return null;
        }
    }
}