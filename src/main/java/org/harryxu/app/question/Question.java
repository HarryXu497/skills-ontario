package org.harryxu.app.question;

import java.util.List;

public record Question (
    Difficulty difficulty,
    String category,
    String question,
    String correctAnswer,
    List<String> answers) {
}

