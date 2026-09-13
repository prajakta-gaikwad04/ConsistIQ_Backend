package com.may26.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.may26.entity.DailyWord;
import com.may26.service.DailyWordsService;

@RestController
@RequestMapping("/api/daily-words")
public class DailyWordsController {

    private final DailyWordsService dailyWordsService;

    public DailyWordsController(
            DailyWordsService dailyWordsService) {
        this.dailyWordsService = dailyWordsService;
    }

    @GetMapping
    public List<DailyWord> getDailyWords() {
        return dailyWordsService.getDailyWords();
    }
}