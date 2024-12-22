package org.harryxu.app.question;

public enum Difficulty {
    EASY("Easy", 1, 8),
    MEDIUM("Medium", 2, 5),
    HARD("Hard", 3, 1);

    private final String text;
    private final int points;
    private final int mistakesAllowed;

    public String getText() {
        return this.text;
    }

    Difficulty(String text, int points, int mistakesAllowed) {
        this.text = text;
        this.points = points;
        this.mistakesAllowed = mistakesAllowed;
    }

    public int getPoints() {
        return this.points;
    }

    public int getMistakesAllowed() {
        return this.mistakesAllowed;
    }

    public static Difficulty parse(String s) {
        return switch (s) {
            case "easy" -> EASY;
            case "medium" -> MEDIUM;
            case "hard" -> HARD;
            default -> throw new IllegalArgumentException("Illegal data (\"" + s + "\").");
        };
    }

    public static Difficulty increment(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> MEDIUM;
            case MEDIUM, HARD -> HARD;
        };
    }

    public static Difficulty decrement(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY, MEDIUM -> EASY;
            case HARD -> MEDIUM;
        };
    }
}