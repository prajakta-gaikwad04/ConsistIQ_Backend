package com.may26.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.may26.entity.DailyWord;
import com.may26.entity.User;

public interface DailyWordRepository
        extends JpaRepository<DailyWord, Long> {

    @Query("""
        SELECT w
        FROM DailyWord w
        WHERE w.id NOT IN (
            SELECT h.word.id
            FROM UserWordHistory h
            WHERE h.user = :user
        )
        ORDER BY w.id
    """)
    List<DailyWord> findWordsNotSeenByUser(
            @Param("user") User user
    );
    Optional<DailyWord> findByWordIgnoreCase(String word);
    long count();
}