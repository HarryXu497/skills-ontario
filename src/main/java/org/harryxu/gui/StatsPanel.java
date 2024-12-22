package org.harryxu.gui;

import org.harryxu.app.Procedure;
import org.harryxu.app.stats.Stats;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Show stats at the end of the game */
public class StatsPanel extends JPanel {
    private final Stats stats;

    private final JLabel correctAnswers;
    private final JLabel currentScore;
    private final JLabel remainingMistakes;

    private final Procedure onContinue;

    public StatsPanel(Stats stats, Procedure onContinue) {
        this.stats = stats;
        this.onContinue = onContinue;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Font plain = new Font("Arial", Font.PLAIN, 18);
        Font bold = plain.deriveFont(Font.BOLD);

        Font buttonFont = plain.deriveFont(Font.BOLD, 20);

        // JLabels
        this.correctAnswers = new JLabel();
        this.currentScore = new JLabel();
        this.remainingMistakes = new JLabel();
        this.correctAnswers.setFont(plain);
        this.currentScore.setFont(plain);
        this.remainingMistakes.setFont(plain);

        // Add labels
        JPanel correctAnswersGroup = new JPanel();
        correctAnswersGroup.setLayout(new BoxLayout(correctAnswersGroup, BoxLayout.X_AXIS));
        JLabel correctAnswersLabel = new JLabel("Correct Answers: ");
        correctAnswersLabel.setFont(bold);
        correctAnswersGroup.add(correctAnswersLabel);
        correctAnswersGroup.add(this.correctAnswers);

        JPanel currentScoreGroup = new JPanel();
        currentScoreGroup.setLayout(new BoxLayout(currentScoreGroup, BoxLayout.X_AXIS));
        JLabel currentScoreLabel = new JLabel("Current Score: ");
        currentScoreLabel.setFont(bold);
        currentScoreGroup.add(currentScoreLabel);
        currentScoreGroup.add(this.currentScore);

        JPanel remainingMistakesGroup = new JPanel();
        remainingMistakesGroup.setLayout(new BoxLayout(remainingMistakesGroup, BoxLayout.X_AXIS));
        JLabel remainingMistakesLabel = new JLabel("Remaining Mistakes: ");
        remainingMistakesLabel.setFont(bold);
        remainingMistakesGroup.add(remainingMistakesLabel);
        remainingMistakesGroup.add(this.remainingMistakes);

        JPanel continuePanel = new JPanel();
        continuePanel.setLayout(new BoxLayout(continuePanel, BoxLayout.X_AXIS));
        JButton continueButton = new JButton("Continue");
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(buttonFont);
        continueButton.setBackground(Const.ACCENT_COLOR);
        continueButton.setBorder(Const.createRaisedBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        continueButton.addActionListener(new OnContinueListener());
        continuePanel.add(continueButton);

        // Add labels to panel
        this.add(Box.createVerticalGlue());
        this.add(correctAnswersGroup);
        this.add(Box.createVerticalStrut(5));
        this.add(currentScoreGroup);
        this.add(Box.createVerticalStrut(5));
        this.add(remainingMistakesGroup);
        this.add(Box.createVerticalStrut(10));
        this.add(continuePanel);
        this.add(Box.createVerticalGlue());

        this.refreshGUI();
    }

    /** Synchronizes the GUI and the {@code stats} property */
    public void refreshGUI() {
        this.correctAnswers.setText(Integer.toString(this.stats.getCorrectCount()));
        this.currentScore.setText(Integer.toString(this.stats.getPoints()));
        this.remainingMistakes.setText(Integer.toString(this.stats.getMistakesLeft()));
    }

    private class OnContinueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            onContinue.run();
        }
    }
}
