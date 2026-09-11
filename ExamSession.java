package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamSession {
    private User user;
    private List<Question> questions;
    private Map<Integer, Integer> answers; // Maps Question ID to selected option index (-1 for unanswered)
    private int totalTimeSeconds;
    private int remainingTimeSeconds;
    private boolean submitted;

    public ExamSession(User user, List<Question> questions, int durationMinutes) {
        this.user = user;
        this.questions = questions;
        this.answers = new HashMap<>();
        for (Question q : questions) {
            answers.put(q.getId(), -1); // Initialize as unanswered
        }
        this.totalTimeSeconds = durationMinutes * 60;
        this.remainingTimeSeconds = this.totalTimeSeconds;
        this.submitted = false;
    }

    public User getUser() {
        return user;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Map<Integer, Integer> getAnswers() {
        return answers;
    }

    public void setAnswer(int questionId, int selectedIndex) {
        answers.put(questionId, selectedIndex);
    }

    public int getAnswer(int questionId) {
        return answers.getOrDefault(questionId, -1);
    }

    public int getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public int getRemainingTimeSeconds() {
        return remainingTimeSeconds;
    }

    public void decrementTime() {
        if (remainingTimeSeconds > 0) {
            remainingTimeSeconds--;
        }
    }

    public boolean isTimeUp() {
        return remainingTimeSeconds <= 0;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public int getScore() {
        int score = 0;
        for (Question q : questions) {
            if (answers.getOrDefault(q.getId(), -1) == q.getCorrectOptionIndex()) {
                score++;
            }
        }
        return score;
    }

    public int getTimeTakenSeconds() {
        return totalTimeSeconds - remainingTimeSeconds;
    }

    public String getTimeTakenFormatted() {
        int taken = getTimeTakenSeconds();
        int minutes = taken / 60;
        int seconds = taken % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
