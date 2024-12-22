package org.harryxu.gui;

import org.harryxu.app.question.Difficulty;
import org.harryxu.app.question.Question;
import org.harryxu.app.stats.Stats;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionsPanel extends JPanel {
    private static final double HELP_BUTTON_THRESHOLD = 0.9;
    private static final Color[] COLORS = new Color[] {
            new Color(235, 13, 57),
            new Color(0,107,202),
            new Color(215,145,32),
            new Color(32,136,30,255)
    };
    private static final Color CORRECT_COLOR = COLORS[3];
    private static final Color INCORRECT_COLOR = COLORS[0];

    private Question question;
    private final Map<Difficulty, List<Question>> questionGroups;
    private final Consumer<Stats> onGameOver;
    private final StatsPanel statsPanel;
    private Difficulty difficulty;
    private final Stats playerStats;

    private final JPanel helperPanel;
    private final JPanel questionPanel;
    private final JLabel questionText;
    private final JPanel buttonPanel;

    private final JButton hintButton;
    private final JButton helpButton;
    private List<JButton> buttons;

    public QuestionsPanel(
            List<Question> questions,
            Difficulty difficulty,
            Consumer<Stats> onGameOver
    ) {
        this.playerStats = new Stats(difficulty.getMistakesAllowed());;
        this.difficulty = difficulty;
        this.onGameOver = onGameOver;

        this.questionGroups = questions.stream()
                .collect(Collectors.groupingBy(Question::difficulty));

        this.buttons = IntStream.range(0, 4).mapToObj(i -> new JButton()).toList();

        this.statsPanel = new StatsPanel(this.playerStats, this::onContinue);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Font buttonFont = new Font("Arial", Font.PLAIN, 22);

        // Helper buttons
        this.hintButton = new JButton("Hint");
        this.helpButton = new JButton("Ask for Help");
        this.hintButton.setToolTipText("Eliminate 2 incorrect answers.");
        this.helpButton.setToolTipText("An answer is suggested which is correct most of the time");
        this.helperPanel = new JPanel();
        this.helperPanel.setLayout(new GridLayout(1, 2, 5, 5));
        this.hintButton.addActionListener(new OnHintListener());
        this.helpButton.addActionListener(new OnHelpListener());

        // Style helper buttons
        for (JButton button : new JButton[] { this.hintButton, this.helpButton }) {
            button.setBackground(Const.ACCENT_COLOR);
            button.setForeground(Color.WHITE);
            button.setFont(buttonFont);
            button.setBorder(Const.createRaisedBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        }

        this.helperPanel.add(this.hintButton);
        this.helperPanel.add(this.helpButton);

        // Create components
        this.questionPanel = new JPanel();
        this.buttonPanel = new JPanel();
        this.questionPanel.setLayout(new BorderLayout());

        this.questionText = new JLabel();
        this.questionText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.questionText.setFont(new Font("Arial", Font.BOLD, 18));
        this.questionText.setHorizontalAlignment(SwingConstants.CENTER);
        this.questionPanel.add(this.questionText, BorderLayout.CENTER);

        this.buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));
        this.buttonPanel.setPreferredSize(new Dimension(Const.WIDTH, Const.HEIGHT / 3));

        // Add buttons to grid
        for (int i = 0; i < this.buttons.size(); i++) {
            JButton button = this.buttons.get(i);
            button.setBackground(COLORS[i]);
            button.setForeground(Color.WHITE);
            this.buttonPanel.add(button);
            button.setBorder(Const.createRaisedBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            button.setFont(buttonFont);
            button.addActionListener(new OnSelectListener(button));
        }

        this.setLayout(new BorderLayout());

        this.add(this.helperPanel, BorderLayout.NORTH);
        this.add(this.questionPanel, BorderLayout.CENTER);
        this.add(this.buttonPanel, BorderLayout.SOUTH);

        this.nextQuestion();
    }

    private void nextQuestion() {
        this.generateQuestion();
        this.updateQuestionUI();
    }

    /** Generates a random question and removes it from further generation  */
    private void generateQuestion() {
        List<Question> availQuestions = this.questionGroups.get(this.difficulty);

        Question q = availQuestions.get((int) (Math.random() * availQuestions.size()));
        availQuestions.remove(q);

        this.question = q;
    }

    /** Updates the UI */
    private void updateQuestionUI() {
        List<String> shuffledAnswers = new ArrayList<>(this.question.answers());
        Collections.shuffle(shuffledAnswers);

        assert shuffledAnswers.size() == this.buttons.size();

        for (int i = 0; i < shuffledAnswers.size(); i++) {
            this.buttons.get(i).setText(shuffledAnswers.get(i));
        }

        this.questionText.setText(
                "<html>" +
                "[" + this.question.difficulty() + "] " +
                this.question.question() +
                "</html>"
        );
    }

    /** Resets the styling and check if the game is over */
    public void onContinue() {
        this.questionPanel.remove(this.statsPanel);
        this.questionPanel.add(this.questionText);

        for (int i = 0; i < this.buttons.size(); i++) {
            JButton button = this.buttons.get(i);
            button.setBackground(COLORS[i]);
            button.setForeground(Color.WHITE);
            button.setEnabled(true);
            button.setBorder(UIManager.getLookAndFeelDefaults().getBorder("Button.border"));
        }

        if (this.playerStats.getMistakesLeft() <= 0) {
            onGameOver.accept(this.playerStats);
        }

        this.questionPanel.revalidate();
        this.questionPanel.repaint();

        nextQuestion();
    }

    private class OnSelectListener implements ActionListener {
        private final JButton answer;

        public OnSelectListener(JButton answer) {
            this.answer = answer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean correct = question.correctAnswer().equals(answer.getText());

            // Check if player is correct
            if (correct) {
                playerStats.incrementDifficultyCorrectCount(question.difficulty());
                playerStats.addPoints(question.difficulty().getPoints());
                playerStats.incrementStreak();
            } else {
                playerStats.incrementDifficultyIncorrectCount(question.difficulty());
                playerStats.incrementMistakes();
                playerStats.resetStreak();
                difficulty = Difficulty.decrement(difficulty);
            }

            if (playerStats.getCurrentStreak() == 3) {
                difficulty = Difficulty.increment(difficulty);
            }

            // Shows correct and incorrect answers
            for (JButton button : buttons) {
                button.setEnabled(false);

                if (button.getText().equals(question.correctAnswer())) {
                    button.setBackground(CORRECT_COLOR);
                    button.setBorder(BorderFactory.createRaisedSoftBevelBorder());
                } else {
                    button.setBackground(INCORRECT_COLOR);
                    button.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                }
            }

            statsPanel.refreshGUI();

            questionPanel.remove(questionText);
            questionPanel.add(statsPanel, BorderLayout.CENTER);
            questionPanel.revalidate();
            questionPanel.repaint();
        }
    }

    /** runs the hint function */
    private class OnHintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<JButton> incorrectAnswerButtons = buttons
                    .stream()
                    .filter(button -> !button.getText().equals(question.correctAnswer()))
                    .collect(Collectors.toList());

            assert incorrectAnswerButtons.size() == 3;

            for (int i = 0; i < 2; i++) {
                int rand = (int) (Math.random() * incorrectAnswerButtons.size());
                JButton button = incorrectAnswerButtons.get(rand);

                button.setEnabled(false);

                incorrectAnswerButtons.remove(button);
            }

            hintButton.setEnabled(false);
        }
    }

    /** runs the help function */
    private class OnHelpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean correct = Math.random() < HELP_BUTTON_THRESHOLD;

            JButton correctButton = null;

            for (JButton button : buttons) {
                if (button.getText().equals(question.correctAnswer())) {
                    correctButton = button;
                }
            }

            assert correctButton != null;

            if (correct) {
                correctButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            } else {
                List<JButton> incorrectButtons = buttons
                    .stream()
                    .filter(button -> !button.getText().equals(question.correctAnswer()))
                    .toList();

                incorrectButtons.get((int) (Math.random() * incorrectButtons.size())).setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            }

            helpButton.setEnabled(false);
        }
    }
}
