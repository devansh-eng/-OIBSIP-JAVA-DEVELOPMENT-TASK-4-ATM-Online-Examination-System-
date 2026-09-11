package ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import app.MainFrame;
import model.ExamSession;
import model.Question;
import ui.Theme;
import ui.components.ModernButton;
import ui.components.ModernPanel;

public class ResultScreen extends JPanel {
    private MainFrame mainFrame;
    private ExamSession session;

    public ResultScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);
    }

    public void setSession(ExamSession session) {
        this.session = session;
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void initUI() {
        int total = session.getQuestions().size();
        int score = session.getScore();
        double percentage = ((double) score / total) * 100;

        // Top Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Theme.BG_DARK);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Exam Results Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.ACCENT);
        summaryPanel.add(titleLabel, BorderLayout.WEST);

        // Logout Button
        ModernButton logoutBtn = new ModernButton("Logout to Portal", Theme.INPUT_BG, Theme.DANGER);
        logoutBtn.setPreferredSize(new Dimension(160, 40));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHeader.setOpaque(false);
        rightHeader.add(logoutBtn);
        summaryPanel.add(rightHeader, BorderLayout.EAST);

        add(summaryPanel, BorderLayout.NORTH);

        // Center Split Panel (Stats on Left, Detailed Review on Right)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column: Stats Card
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new java.awt.Insets(0, 0, 0, 15);

        ModernPanel statsCard = new ModernPanel(20, Theme.CARD_BG);
        statsCard.setLayout(new BoxLayout(statsCard, BoxLayout.Y_AXIS));
        statsCard.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        JLabel studentLabel = new JLabel(session.getUser().getDisplayName());
        studentLabel.setFont(Theme.SUBTITLE_FONT);
        studentLabel.setForeground(Theme.TEXT_MAIN);
        studentLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subStudentLabel = new JLabel("@" + session.getUser().getUsername());
        subStudentLabel.setFont(Theme.SMALL_FONT);
        subStudentLabel.setForeground(Theme.TEXT_MUTED);
        subStudentLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Percentage Circle/Label
        JLabel scoreCircle = new JLabel(String.format("%.0f%%", percentage), SwingConstants.CENTER);
        scoreCircle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 48));
        scoreCircle.setForeground(percentage >= 50.0 ? Theme.SUCCESS : Theme.DANGER);
        scoreCircle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel scoreSummary = new JLabel("Score: " + score + " / " + total);
        scoreSummary.setFont(Theme.HEADER_FONT);
        scoreSummary.setForeground(Theme.TEXT_MAIN);
        scoreSummary.setAlignmentX(CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("Time Taken: " + session.getTimeTakenFormatted());
        timeLabel.setFont(Theme.BODY_FONT);
        timeLabel.setForeground(Theme.TEXT_MUTED);
        timeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Build stats display
        statsCard.add(studentLabel);
        statsCard.add(subStudentLabel);
        statsCard.add(Box.createRigidArea(new Dimension(0, 30)));
        statsCard.add(scoreCircle);
        statsCard.add(Box.createRigidArea(new Dimension(0, 10)));
        statsCard.add(scoreSummary);
        statsCard.add(Box.createRigidArea(new Dimension(0, 15)));
        statsCard.add(timeLabel);
        statsCard.add(Box.createVerticalGlue());

        // Restart exam option for debugging/demo
        ModernButton retakeBtn = new ModernButton("Retake Exam", Theme.ACCENT, Theme.TEXT_DARK);
        retakeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        retakeBtn.setAlignmentX(CENTER_ALIGNMENT);
        retakeBtn.addActionListener(e -> mainFrame.showProfile(session.getUser()));
        statsCard.add(retakeBtn);

        centerPanel.add(statsCard, gbc);

        // Right Column: Scrollable breakdown list
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);

        JLabel breakdownTitle = new JLabel("Questions Review");
        breakdownTitle.setFont(Theme.HEADER_FONT);
        breakdownTitle.setForeground(Theme.TEXT_MAIN);
        breakdownTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        scrollWrapper.add(breakdownTitle, BorderLayout.NORTH);

        // Container panel inside scrollpane
        JPanel listContainer = new JPanel();
        listContainer.setBackground(Theme.BG_DARK);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        List<Question> questions = session.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int selected = session.getAnswer(q.getId());
            boolean isCorrect = selected == q.getCorrectOptionIndex();

            ModernPanel itemPanel = new ModernPanel(12, Theme.CARD_BG);
            itemPanel.setLayout(new BorderLayout(15, 0));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

            // Status Indicator Label (Left)
            JLabel indicatorLabel = new JLabel(isCorrect ? "✔" : "✘", SwingConstants.CENTER);
            indicatorLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
            indicatorLabel.setForeground(isCorrect ? Theme.SUCCESS : Theme.DANGER);
            indicatorLabel.setPreferredSize(new Dimension(30, 30));
            itemPanel.add(indicatorLabel, BorderLayout.WEST);

            // Details panel (Center)
            JPanel textDetails = new JPanel();
            textDetails.setOpaque(false);
            textDetails.setLayout(new BoxLayout(textDetails, BoxLayout.Y_AXIS));

            JLabel qNum = new JLabel("Question " + (i + 1));
            qNum.setFont(Theme.SMALL_FONT);
            qNum.setForeground(Theme.ACCENT);

            JLabel qText = new JLabel("<html><body style='width: 320px;'>" + q.getQuestionText() + "</body></html>");
            qText.setFont(Theme.BODY_BOLD);
            qText.setForeground(Theme.TEXT_MAIN);

            String selectedStr = (selected == -1) ? "Unanswered" : q.getOptions()[selected];
            JLabel userAnsLabel = new JLabel("Your Answer: " + selectedStr);
            userAnsLabel.setFont(Theme.BODY_FONT);
            userAnsLabel.setForeground(isCorrect ? Theme.SUCCESS : Theme.DANGER);

            JLabel correctAnsLabel = new JLabel("Correct Answer: " + q.getOptions()[q.getCorrectOptionIndex()]);
            correctAnsLabel.setFont(Theme.BODY_FONT);
            correctAnsLabel.setForeground(Theme.TEXT_MUTED);

            textDetails.add(qNum);
            textDetails.add(qText);
            textDetails.add(Box.createRigidArea(new Dimension(0, 4)));
            textDetails.add(userAnsLabel);
            if (!isCorrect) {
                textDetails.add(correctAnsLabel);
            }

            itemPanel.add(textDetails, BorderLayout.CENTER);

            listContainer.add(itemPanel);
            listContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getViewport().setBackground(Theme.BG_DARK);

        scrollWrapper.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(scrollWrapper, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
}
