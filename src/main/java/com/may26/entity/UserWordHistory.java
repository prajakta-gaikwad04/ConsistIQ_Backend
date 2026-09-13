package com.may26.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_word_history",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "word_id"}
        )
    }
)
public class UserWordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word_id", nullable = false)
    private DailyWord word;

    @Column(name = "shown_date", nullable = false)
    private LocalDate shownDate;

    public UserWordHistory() {
    }

    public UserWordHistory(User user, DailyWord word, LocalDate shownDate) {
        this.user = user;
        this.word = word;
        this.shownDate = shownDate;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DailyWord getWord() {
        return word;
    }

    public void setWord(DailyWord word) {
        this.word = word;
    }

    public LocalDate getShownDate() {
        return shownDate;
    }

    public void setShownDate(LocalDate shownDate) {
        this.shownDate = shownDate;
    }
}