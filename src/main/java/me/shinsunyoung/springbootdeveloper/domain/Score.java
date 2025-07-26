package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId; // 점수 ID

    @ManyToOne // User와의 다대일 관계
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user; // 점수를 기록한 사용자

    @ManyToOne // QuizQuestion과의 다대일 관계
    @JoinColumn(name = "quiz_question_id", referencedColumnName = "quizQuestionId", nullable = false)
    private QuizQuestion quizQuestion; // QuizQuestion 엔티티와의 관계

    private String scoreQuestion; // 질문
    private String scoreOptions; // 선택지
    private String scoreAnswer; // 선택한 답

    private Long scoreCorrect; // 점수

    private LocalDateTime scoreDate; // 점수를 딴 날짜

    // 기본 생성자
    public Score() {}

    // 생성자
    public Score(QuizQuestion quizQuestion, String scoreAnswer, Long scoreCorrect) {
        this.quizQuestion = quizQuestion;
        this.scoreQuestion = quizQuestion.getQuestion();
        this.scoreOptions = quizQuestion.getOptions();
        this.scoreAnswer = scoreAnswer;
        this.scoreCorrect = scoreCorrect;
        this.scoreDate = LocalDateTime.now(); // 현재 날짜와 시간으로 초기화
    }

    // Getters 및 Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
        this.scoreQuestion = quizQuestion.getQuestion();
        this.scoreOptions = quizQuestion.getOptions();
    }

    public String getScoreQuestion() {
        return scoreQuestion;
    }

    public void setScoreQuestion(String scoreQuestion) {
        this.scoreQuestion = scoreQuestion;
    }

    public Long getScoreCorrect() {
        return scoreCorrect;
    }

    public void setScoreCorrect(Long scoreCorrect) {
        this.scoreCorrect = scoreCorrect;
    }

    public String getScoreOptions() {
        return scoreOptions;
    }

    public void setScoreOptions(String scoreOptions) {
        this.scoreOptions = scoreOptions;
    }

    public String getScoreAnswer() {
        return scoreAnswer;
    }

    public void setScoreAnswer(String scoreAnswer) {
        this.scoreAnswer = scoreAnswer;
    }

    public LocalDateTime getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDateTime scoreDate) {
        this.scoreDate = scoreDate;
    }
}
