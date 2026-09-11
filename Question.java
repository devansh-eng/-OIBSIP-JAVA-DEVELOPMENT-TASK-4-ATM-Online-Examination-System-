package model;

public class Question {
    private int id;
    private String questionText;
    private String[] options;
    private int correctOptionIndex;

    public Question(int id, String questionText, String[] options, int correctOptionIndex) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}
