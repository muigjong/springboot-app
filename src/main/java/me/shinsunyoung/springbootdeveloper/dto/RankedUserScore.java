package me.shinsunyoung.springbootdeveloper.dto;

import me.shinsunyoung.springbootdeveloper.domain.User;

public class RankedUserScore {
    private User user;
    private Long totalScore;
    private int rank; // rank 속성 추가

    public RankedUserScore(User user, Long totalScore, int rank) {
        this.user = user;
        this.totalScore = totalScore;
        this.rank = rank; // rank 초기화
    }

    public User getUser() {
        return user;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public int getRank() { // rank에 대한 getter 메서드
        return rank;
    }
}



