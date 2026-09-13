package com.may26.service;

import org.springframework.stereotype.Service;

import com.may26.dto.DictionaryWordResponse;

@Service
public class WordValidationService {

    private final DictionaryApiService dictionaryApiService;

    public WordValidationService(
            DictionaryApiService dictionaryApiService) {

        this.dictionaryApiService = dictionaryApiService;
    }

    public DictionaryWordResponse validateWord(String word) {

        if (word == null || word.isBlank()) {
            return null;
        }

        DictionaryWordResponse result =
                dictionaryApiService.getWord(word);

        if (result == null) {
            return null;
        }

        if (result.getWord() == null
                || result.getWord().isBlank()) {
            return null;
        }

        if (result.getMeaning() == null
                || result.getMeaning().isBlank()) {
            return null;
        }

        if (result.getExample() == null
                || result.getExample().isBlank()) {
            return null;
        }

        return result;
    }
}