package org.harryxu.gui;

import org.harryxu.app.question.Difficulty;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

public class GameStartPanel extends JPanel {
    private final JTextField name;
    private final JComboBox<String> difficulty;
    private final JButton submitButton;

    private final BiConsumer<String, Difficulty> onSubmit;

    public GameStartPanel(BiConsumer<String, Difficulty> onSubmit) {
        this.onSubmit = onSubmit;

        JPanel wrapper = new JPanel();
        Font font = new Font("Arial", Font.PLAIN, 16);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.name = new JTextField(10);
        this.name.setFont(font);
        this.difficulty = new JComboBox<>(new String[] {
                "Easy",
                "Medium",
                "Hard",
        });
        this.difficulty.setFont(font);

        this.submitButton = new JButton("Start Game");
        this.submitButton.addActionListener(new OnSubmitListener());

        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        // Create form
        // Name input
        wrapper.add(Box.createVerticalGlue());
        JPanel nameControl = new JPanel();
        nameControl.setLayout(new BoxLayout(nameControl, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(font);
        nameControl.add(nameLabel);
        nameControl.add(Box.createHorizontalStrut(10));
        nameControl.add(this.name);
        nameControl.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        wrapper.add(nameControl);

        // Padding
        wrapper.add(Box.createVerticalStrut(10));

        // Difficulty input
        JPanel difficultyControl = new JPanel();
        difficultyControl.setLayout(new BoxLayout(difficultyControl, BoxLayout.X_AXIS));
        JLabel difficultyLabel = new JLabel("Select the difficulty:");
        difficultyLabel.setFont(font);
        difficultyControl.add(difficultyLabel);
        difficultyControl.add(Box.createHorizontalStrut(10));
        difficultyControl.add(this.difficulty);
        difficultyControl.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        wrapper.add(difficultyControl);

        // Padding
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.setAlignmentY(0.5f);
        wrapper.setAlignmentX(0.5f);

        // Submit button
        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new BoxLayout(submitPanel, BoxLayout.X_AXIS));
        submitPanel.add(Box.createHorizontalStrut(10));
        submitPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        submitPanel.add(this.submitButton);
        // Change button appearance
        this.submitButton.setFont(font.deriveFont(Font.BOLD, 22));
        this.submitButton.setBackground(Const.ACCENT_COLOR);
        this.submitButton.setForeground(Color.WHITE);
        this.submitButton.setBorder(Const.createRaisedBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        wrapper.add(submitPanel);
        wrapper.add(Box.createVerticalGlue());

        // Title JLabel
        JLabel title = new JLabel("Trivia!");
        title.setFont(font.deriveFont(Font.BOLD, 54));
        title.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Set size
        wrapper.setMaximumSize(new Dimension(Math.max(
                nameControl.getPreferredSize().width,
                difficultyControl.getPreferredSize().width
        ), 200));

        // Add to panel
        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(20));
        this.add(wrapper);
        this.add(Box.createVerticalGlue());
    }

    private class OnSubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Run when form submitted

            String nameData = name.getText().trim();
            Difficulty difficultyData = Difficulty.parse(
                    ((String) difficulty.getSelectedItem()).toLowerCase()
            );

            // Validation
            if (nameData.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid name.");
                return;
            }

            // Call callback
            onSubmit.accept(nameData, difficultyData);
        }
    }
}
