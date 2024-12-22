package org.harryxu.gui;

import org.harryxu.app.question.Difficulty;
import org.harryxu.app.question.Question;
import org.harryxu.app.stats.Stats;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

public class GamePanel extends JPanel {
    private final GameStartPanel gameStartPanel;
    private QuestionsPanel questionsPanel;

    private final List<Question> questions;

    private String name;

    public GamePanel(List<Question> questions) {
        this.questions = questions;
        this.gameStartPanel = new GameStartPanel(this::onGameStart);
        this.setLayout(new BorderLayout());

        this.add(this.gameStartPanel, BorderLayout.CENTER);
    }

    public void replaceScreen(JPanel prev, JPanel curr) {
        this.remove(prev);
        this.revalidate();
        this.repaint();

        this.add(curr, BorderLayout.CENTER);
    }

    // Starts the questions
    public void onGameStart(String name, Difficulty difficulty) {
        this.name = name;

        this.questionsPanel = new QuestionsPanel(this.questions, difficulty, this::onGameOver);

        this.replaceScreen(this.gameStartPanel, this.questionsPanel);
    }

    // End questions and show stats
    public void onGameOver(Stats playerStats) {
        GameEndPanel gameEndPanel = new GameEndPanel(this.name, playerStats);

        this.replaceScreen(this.questionsPanel, gameEndPanel);
    }
}
