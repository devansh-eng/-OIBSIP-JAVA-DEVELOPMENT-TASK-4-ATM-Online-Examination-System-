package ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.SwingConstants;

import app.MainFrame;
import model.ExamSession;
import model.Question;
import ui.Theme;
import ui.components.ModernButton;
import ui.components.ModernPanel;

public class ExamScreen extends JPanel {
    private MainFrame mainFrame;
    private ExamSession session;
    private int currentQuestionIndex = 0;

    // UI elements
    private JLabel timerLabel;
    private JLabel progressLabel;
    private JPanel questionContainer;
    private JLabel questionNumLabel;
    private JLabel questionTextLabel;
    private JRadioButton[] optionRadioButtons;
    private ButtonGroup optionGroup;

    private ModernButton prevButton;
    private ModernButton nextButton;
    private ModernButton submitButton;

    private JPanel gridPanel;
    private ModernButton[] gridButtons;
    private Timer swingTimer;

    public ExamScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);
    }

    public void startSession(ExamSession session) {
        this.session = session;
        this.currentQuestionIndex = 0;
        removeAll();
        initUI();
        loadQuestion(0);
        startTimer();
        revalidate();
        repaint();
    }

    private void initUI() {
        // TOP PANEL: Timer, Progress bar, Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Online Examination System - Active Exam", SwingConstants.LEFT);
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.TEXT_MUTED);

        timerLabel = new JLabel("00:30:00", SwingConstants.RIGHT);
        timerLabel.setFont(Theme.TIMER_FONT);
        timerLabel.setForeground(Theme.ACCENT);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel timerContainer = new JPanel(new BorderLayout());
        timerContainer.setOpaque(false);
        JLabel timerIcon = new JLabel("Time Remaining: ");
        timerIcon.setFont(Theme.BODY_BOLD);
        timerIcon.setForeground(Theme.TEXT_MUTED);
        timerContainer.add(timerIcon, BorderLayout.WEST);
        timerContainer.add(timerLabel, BorderLayout.CENTER);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(timerContainer, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // SIDEBAR: Question Navigation Grid
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.INPUT_BG);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebarPanel.setPreferredSize(new Dimension(220, 0));

        JLabel sidebarTitle = new JLabel("Questions Overview");
        sidebarTitle.setFont(Theme.BODY_BOLD);
        sidebarTitle.setForeground(Theme.TEXT_MAIN);
        sidebarTitle.setAlignmentX(LEFT_ALIGNMENT);

        progressLabel = new JLabel("Answered: 0 / 10");
        progressLabel.setFont(Theme.SMALL_FONT);
        progressLabel.setForeground(Theme.TEXT_MUTED);
        progressLabel.setAlignmentX(LEFT_ALIGNMENT);

        // Question quick-select grid
        gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        int totalQuestions = session.getQuestions().size();
        int rows = (int) Math.ceil(totalQuestions / 4.0);
        gridPanel.setLayout(new GridLayout(rows, 4, 8, 8));

        gridButtons = new ModernButton[totalQuestions];
        for (int i = 0; i < totalQuestions; i++) {
            final int index = i;
            ModernButton btn = new ModernButton(String.valueOf(i + 1), Theme.CARD_BG, Theme.TEXT_MAIN);
            btn.setFont(Theme.SMALL_FONT);
            btn.setPreferredSize(new Dimension(40, 40));
            btn.setCornerRadius(8);
            btn.addActionListener(e -> {
                saveSelectedAnswer();
                loadQuestion(index);
            });
            gridButtons[i] = btn;
            gridPanel.add(btn);
        }

        sidebarPanel.add(sidebarTitle);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(progressLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(gridPanel);
        sidebarPanel.add(Box.createVerticalGlue());

        add(sidebarPanel, BorderLayout.WEST);

        // CENTER PANEL: Question Card & MCQ options
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Theme.BG_DARK);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        ModernPanel questionCard = new ModernPanel(20, Theme.CARD_BG);
        questionCard.setLayout(new BorderLayout());
        questionCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Question Details
        JPanel questionTextPanel = new JPanel();
        questionTextPanel.setOpaque(false);
        questionTextPanel.setLayout(new BoxLayout(questionTextPanel, BoxLayout.Y_AXIS));

        questionNumLabel = new JLabel("Question 1 of 10");
        questionNumLabel.setFont(Theme.BODY_BOLD);
        questionNumLabel.setForeground(Theme.ACCENT);

        questionTextLabel = new JLabel("Question text placeholder?");
        questionTextLabel.setFont(Theme.HEADER_FONT);
        questionTextLabel.setForeground(Theme.TEXT_MAIN);
        // Enable multi-line text wrapping for JLabels
        questionTextLabel.setVerticalAlignment(SwingConstants.TOP);

        questionTextPanel.add(questionNumLabel);
        questionTextPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        questionTextPanel.add(questionTextLabel);
        questionTextPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Radio buttons for MCQ options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        optionRadioButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionRadioButtons[i] = new JRadioButton();
            optionRadioButtons[i].setOpaque(false);
            optionRadioButtons[i].setFont(Theme.BODY_FONT);
            optionRadioButtons[i].setForeground(Theme.TEXT_MAIN);
            optionRadioButtons[i].setFocusPainted(false);
            optionRadioButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            optionGroup.add(optionRadioButtons[i]);
            
            // Add radio button to layout with some space
            optionsPanel.add(optionRadioButtons[i]);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        questionCard.add(questionTextPanel, BorderLayout.NORTH);
        questionCard.add(optionsPanel, BorderLayout.CENTER);

        // NAVIGATION PANEL: Prev, Next, Submit
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftNav.setOpaque(false);
        prevButton = new ModernButton("Previous", Theme.INPUT_BG, Theme.TEXT_MAIN);
        prevButton.setPreferredSize(new Dimension(120, 42));
        prevButton.addActionListener(e -> navigateQuestion(-1));
        leftNav.add(prevButton);

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightNav.setOpaque(false);

        nextButton = new ModernButton("Next", Theme.INPUT_BG, Theme.TEXT_MAIN);
        nextButton.setPreferredSize(new Dimension(120, 42));
        nextButton.addActionListener(e -> navigateQuestion(1));
        
        submitButton = new ModernButton("Submit Exam", Theme.SUCCESS, Theme.TEXT_DARK);
        submitButton.setPreferredSize(new Dimension(140, 42));
        submitButton.addActionListener(e -> confirmSubmitExam());

        rightNav.add(nextButton);
        rightNav.add(submitButton);

        navPanel.add(leftNav, BorderLayout.WEST);
        navPanel.add(rightNav, BorderLayout.EAST);

        mainContent.add(questionCard, BorderLayout.CENTER);
        mainContent.add(navPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private void loadQuestion(int index) {
        currentQuestionIndex = index;
        List<Question> questions = session.getQuestions();
        Question question = questions.get(index);

        // Update Labels
        questionNumLabel.setText("Question " + (index + 1) + " of " + questions.size());
        
        // Wrap question text in HTML to support multiline wrap in JLabel
        questionTextLabel.setText("<html><body style='width: 450px;'>" + question.getQuestionText() + "</body></html>");

        // Clear option selection and set options text
        optionGroup.clearSelection();
        String[] options = question.getOptions();
        for (int i = 0; i < 4; i++) {
            optionRadioButtons[i].setText(options[i]);
        }

        // Pre-select if answer already saved
        int savedAnswer = session.getAnswer(question.getId());
        if (savedAnswer >= 0 && savedAnswer < 4) {
            optionRadioButtons[savedAnswer].setSelected(true);
        }

        // Enable/Disable buttons based on indices
        prevButton.setEnabled(index > 0);
        nextButton.setVisible(index < questions.size() - 1);

        updateSidebarProgress();
        updateSidebarGridHighlights();
    }

    private void saveSelectedAnswer() {
        if (session == null || currentQuestionIndex < 0 || currentQuestionIndex >= session.getQuestions().size()) {
            return;
        }
        Question currentQuestion = session.getQuestions().get(currentQuestionIndex);
        int selectedIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (optionRadioButtons[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        session.setAnswer(currentQuestion.getId(), selectedIndex);
    }

    private void navigateQuestion(int direction) {
        saveSelectedAnswer();
        int newIndex = currentQuestionIndex + direction;
        if (newIndex >= 0 && newIndex < session.getQuestions().size()) {
            loadQuestion(newIndex);
        }
    }

    private void updateSidebarProgress() {
        int answered = 0;
        for (Question q : session.getQuestions()) {
            if (session.getAnswer(q.getId()) != -1) {
                answered++;
            }
        }
        progressLabel.setText("Answered: " + answered + " / " + session.getQuestions().size());
    }

    private void updateSidebarGridHighlights() {
        for (int i = 0; i < gridButtons.length; i++) {
            Question q = session.getQuestions().get(i);
            int selectedAns = session.getAnswer(q.getId());

            if (i == currentQuestionIndex) {
                // Current question highlight
                gridButtons[i].setBackground(Theme.ACCENT);
                gridButtons[i].setForeground(Theme.TEXT_DARK);
                gridButtons[i].setBorderColor(Theme.TEXT_MAIN);
            } else if (selectedAns != -1) {
                // Answered question
                gridButtons[i].setBackground(Theme.SUCCESS);
                gridButtons[i].setForeground(Theme.TEXT_DARK);
                gridButtons[i].setBorderColor(null);
            } else {
                // Unanswered question
                gridButtons[i].setBackground(Theme.CARD_BG);
                gridButtons[i].setForeground(Theme.TEXT_MUTED);
                gridButtons[i].setBorderColor(Theme.BORDER_COLOR);
            }
        }
    }

    private void startTimer() {
        if (swingTimer != null && swingTimer.isRunning()) {
            swingTimer.stop();
        }

        swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                session.decrementTime();
                updateTimerLabel();

                if (session.isTimeUp()) {
                    swingTimer.stop();
                    autoSubmitExam();
                }
            }
        });
        updateTimerLabel();
        swingTimer.start();
    }

    public void stopTimer() {
        if (swingTimer != null) {
            swingTimer.stop();
        }
    }

    private void updateTimerLabel() {
        int seconds = session.getRemainingTimeSeconds();
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", h, m, s));

        // Warn when timer is low (less than 1 minute)
        if (seconds < 60) {
            timerLabel.setForeground(Theme.DANGER);
        } else if (seconds < 300) {
            timerLabel.setForeground(Theme.WARNING);
        } else {
            timerLabel.setForeground(Theme.ACCENT);
        }
    }

    private void confirmSubmitExam() {
        saveSelectedAnswer();
        updateSidebarProgress();

        int unanswered = 0;
        for (Question q : session.getQuestions()) {
            if (session.getAnswer(q.getId()) == -1) {
                unanswered++;
            }
        }

        String confirmMessage = "Are you sure you want to submit the exam?";
        if (unanswered > 0) {
            confirmMessage += "\nWarning: You have left " + unanswered + " questions unanswered!";
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            confirmMessage,
            "Confirm Submission",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            submitExam();
        }
    }

    private void autoSubmitExam() {
        saveSelectedAnswer();
        JOptionPane.showMessageDialog(
            this,
            "Time is up! Your exam will be submitted automatically.",
            "Time Expired",
            JOptionPane.WARNING_MESSAGE
        );
        submitExam();
    }

    private void submitExam() {
        stopTimer();
        session.setSubmitted(true);
        mainFrame.showResults(session);
    }
}
