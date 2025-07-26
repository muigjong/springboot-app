package me.shinsunyoung.springbootdeveloper.dto;

public class UserScore {
    private String nickname;
    private Long totalScore;

    public UserScore(String nickname, Long totalScore) {
        this.nickname = nickname;
        this.totalScore = totalScore;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getTotalScore() {
        return totalScore;
    }
}
