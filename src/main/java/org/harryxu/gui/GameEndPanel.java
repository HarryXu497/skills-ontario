package org.harryxu.gui;

import org.harryxu.app.question.Difficulty;
import org.harryxu.app.stats.Stats;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

/** Display player statistics at the end of the game */
public class GameEndPanel extends JPanel {
    public GameEndPanel(String name, Stats playerStats) {
        Font plain = new Font("Arial", Font.PLAIN, 16);
        Font bold = new Font("Arial", Font.BOLD, 20);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(
            name + "'s Statistics"
        );

        nameLabel.setFont(bold);
        nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel pointsLabel = new JLabel(
        "<html>" +
                "Total Points: <strong>" + playerStats.getPoints() + "</strong>" +
            "</html>"
        );

        pointsLabel.setFont(plain);
        pointsLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel questionsLabel = new JLabel(
            "<html>" +
                    "Correct Answers: <strong>" + playerStats.getCorrectCount() + "</strong>" +
                    "<ul>" +
                        "<li>Easy Questions: <strong>" + playerStats.getCorrectByDifficulty(Difficulty.EASY) + "</strong></li>" +
                        "<li>Medium Questions: <strong>" + playerStats.getCorrectByDifficulty(Difficulty.MEDIUM) + "</strong></li>" +
                        "<li>Hard Questions: <strong>" + playerStats.getCorrectByDifficulty(Difficulty.HARD) + "</strong></li>" +
                    "</ul>" +
                    "Incorrect Answers: <strong>" + playerStats.getIncorrectCount() + "</strong>" +
                        "<ul>" +
                        "<li>Easy Questions: <strong>" + playerStats.getIncorrectByDifficulty(Difficulty.EASY) + "</strong></li>" +
                        "<li>Medium Questions: <strong>" + playerStats.getIncorrectByDifficulty(Difficulty.MEDIUM) + "</strong></li>" +
                        "<li>Hard Questions: <strong>" + playerStats.getIncorrectByDifficulty(Difficulty.HARD) + "</strong></li>" +
                    "</ul>" +
                "</html>"
        );
        questionsLabel.setFont(plain);
        questionsLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JPanel wrapper = getWrapperPanel(nameLabel, pointsLabel, questionsLabel);

        // Add labels to wrapper container
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(nameLabel);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(pointsLabel);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(questionsLabel);
        wrapper.add(Box.createVerticalGlue());

        this.add(wrapper, BorderLayout.CENTER);
    }

    private static JPanel getWrapperPanel(JLabel nameLabel, JLabel pointsLabel, JLabel questionsLabel) {
        int wrapperWidth = Integer.MIN_VALUE;

        // Gets max width of children
        int[] widths = {
                nameLabel.getPreferredSize().width,
                pointsLabel.getPreferredSize().width,
                questionsLabel.getPreferredSize().width
        };

        for (int width : widths) {
            wrapperWidth = Math.max(wrapperWidth, width);
        }

        JPanel wrapper = new JPanel();
        wrapper.setMaximumSize(new Dimension(wrapperWidth, 600));
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        return wrapper;
    }
}
