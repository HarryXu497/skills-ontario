package org.harryxu.app.stats;

import org.harryxu.app.question.Difficulty;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private int currentStreak;
    private int points;
    private int correctCount;
    private int incorrectCount;
    private int mistakesLeft;
    private final Map<Difficulty, Integer> correctByDifficulty;
    private final Map<Difficulty, Integer> incorrectByDifficulty;

    public Stats(int mistakesAllowed) {
        this.points = 0;
        this.currentStreak = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.mistakesLeft = mistakesAllowed;
        this.correctByDifficulty = new HashMap<>();
        this.incorrectByDifficulty = new HashMap<>();

        for (Difficulty difficulty : Difficulty.values()) {
            this.correctByDifficulty.put(difficulty, 0);
            this.incorrectByDifficulty.put(difficulty, 0);
        }
    }

    public void incrementStreak() {
        this.currentStreak++;
    }

    public void resetStreak() {
        this.currentStreak = 0;
    }

    public void addPoints(int p) {
        if (p <= 0) {
            throw new IllegalArgumentException("The amount of points added must be strictly positive.");
        }

        this.points += p;
    }

    public void incrementMistakes() {
        this.mistakesLeft--;
    }

    public void incrementDifficultyCorrectCount(Difficulty difficulty) {
        this.correctByDifficulty.computeIfPresent(difficulty, (k, v) -> v + 1);

        this.correctCount++;
    }

    public void incrementDifficultyIncorrectCount(Difficulty difficulty) {
        this.incorrectByDifficulty.computeIfPresent(difficulty, (k, v) -> v + 1);

        this.incorrectCount++;
    }


    public int getCorrectByDifficulty(Difficulty difficulty) {
        return this.correctByDifficulty.get(difficulty);
    }

    public int getIncorrectByDifficulty(Difficulty difficulty) {
        return this.incorrectByDifficulty.get(difficulty);
    }

    public int getCurrentStreak() {
        return this.currentStreak;
    }

    public int getPoints() {
        return this.points;
    }

    public int getMistakesLeft() {
        return this.mistakesLeft;
    }

    public int getCorrectCount() {
        return this.correctCount;
    }

    public int getIncorrectCount() {
        return this.incorrectCount;
    }
}
