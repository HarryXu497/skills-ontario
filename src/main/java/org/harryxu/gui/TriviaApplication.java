package org.harryxu.gui;

import org.harryxu.app.question.Question;

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.util.List;

public class TriviaApplication {
    private final List<Question> questions;
    private final JFrame frame;

    public TriviaApplication(List<Question> questions) {
        this.questions = questions;

        this.frame = new JFrame("Trivia!");
        this.frame.setSize(Const.WIDTH, Const.HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.setLayout(new BorderLayout());

        this.frame.add(new GamePanel(questions), BorderLayout.CENTER);
    }

    public void start() {
        this.frame.setVisible(true);
    }
}
