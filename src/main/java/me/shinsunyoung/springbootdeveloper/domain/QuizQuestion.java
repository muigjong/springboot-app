package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizQuestionId;

    private String question;

    private String options; // String 타입으로 변경

    private String correctAnswer; // String으로 변경 (정답을 문자열로 저장)

    private String commentary; // String으로 변경

    private String sort; // String으로 변경



    @Transient
    private String userAnswer; // 사용자가 선택한 답안
    @Transient
    private boolean isCorrect; // 정답 여부

    // Getters and Setters
    public Long getQuizQuestionId() {
        return quizQuestionId;
    }

    public void setQuizQuestionId(Long quizQuestionId) {
        this.quizQuestionId = quizQuestionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }



    // options에 대한 getter와 setter
    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    // String options를 List<String>으로 변환하는 메서드
    public List<String> getOptionsList() {
        if (options != null && !options.isEmpty()) {
            return Arrays.asList(options.split(",\\s*")); // 쉼표와 공백으로 분리하여 리스트로 반환
        } else {
            return new ArrayList<>(); // 빈 리스트 반환
        }
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}




