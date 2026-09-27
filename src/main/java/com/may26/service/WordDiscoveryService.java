package com.may26.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WordDiscoveryService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WordDiscoveryService() {

        this.restClient = RestClient
                .builder()
                .baseUrl("https://api.datamuse.com")
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public List<String> findWords() {

        // ALWAYS return a mutable list
        List<String> words = new ArrayList<>();

        try {

            String json = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/words")
                            .queryParam(
                                    "ml",
                                    "useful words for communication"
                            )
                            .queryParam("max", 20)
                            .build())
                    .retrieve()
                    .body(String.class);

            if (json == null || json.isBlank()) {
                return words;
            }

            JsonNode root =
                    objectMapper.readTree(json);

            if (!root.isArray()) {
                return words;
            }

            for (JsonNode item : root) {

                String word =
                        item.path("word")
                                .asText("")
                                .trim()
                                .toLowerCase();

                if (isValidCandidate(word)
                        && !words.contains(word)) {

                    words.add(word);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Datamuse unavailable: "
                            + e.getMessage()
            );
        }

        return words;
    }

    private boolean isValidCandidate(String word) {

        if (word == null || word.isBlank()) {
            return false;
        }

        if (!word.matches("[a-zA-Z]+")) {
            return false;
        }

        if (word.length() < 4
                || word.length() > 15) {
            return false;
        }

        return true;
    }
}