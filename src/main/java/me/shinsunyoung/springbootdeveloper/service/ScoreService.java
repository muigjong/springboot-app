package me.shinsunyoung.springbootdeveloper.service;


import me.shinsunyoung.springbootdeveloper.domain.QuizQuestion;
import me.shinsunyoung.springbootdeveloper.domain.Score;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.RankedUserScore;
import me.shinsunyoung.springbootdeveloper.dto.UserScore;
import me.shinsunyoung.springbootdeveloper.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;


    // 문제마다 풀 때 주어지는 점수
    public void saveScore(Score score) {
        score.setScoreDate(LocalDateTime.now()); // 점수를 딴 날짜를 현재 시간으로 설정
        scoreRepository.save(score);
    }

// 각 회원의 총점수
    public Long calculateTotalScore(User user) {
        return scoreRepository.findTotalScoreByUserId(user.getUserId());
    }

    // 특정 교회에 소속된 회원과 점수를 정렬
    public List<User> findUsersByChurchOrderedByTotalScore(String church) {
        return scoreRepository.findUsersByChurchOrderedByTotalScore(church);
    }


//    교회의 총점수
    public Long getTotalScoreByChurch(String church) {
        List<Score> scores = scoreRepository.findScoresByChurchOrdered(church);
        return scores.stream().mapToLong(Score::getScoreCorrect).sum();
    }

//    특정 교회 회원들을 정렬
public List<UserScore> getUniqueUserScores(List<Score> scores) {
    if (scores == null || scores.isEmpty()) {
        return List.of(); // Return empty list if scores is null or empty
    }

    // Group by nickname and sum scores
    return scores.stream()
            .collect(Collectors.groupingBy(
                    score -> score.getUser().getNickname(),
                    Collectors.summingLong(score -> score.getScoreCorrect()) // 점수를 바로 합산
            ))
            .entrySet().stream()
            .map(entry -> new UserScore(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
}


// 총점수순으로 정렬

    public List<Score> getScoresByChurchOrdered(String church) {
        return scoreRepository.findScoresByChurchOrdered(church);
    }


    public Long getTotalScoreForChurch(String churchName) {
        return scoreRepository.sumScoresByChurch(churchName);
    }

    public Map<String, Long> getTotalScoresByAllChurches() {
        List<Object[]> results = scoreRepository.sumScoresByAllChurches();
        Map<String, Long> churchScores = new HashMap<>();

        for (Object[] result : results) {
            String church = (String) result[0];
            Long totalScore = (Long) result[1];
            churchScores.put(church, totalScore);
        }

        return churchScores;
    }

//    이벤트
public List<Score> getScoresForEvent(User user, LocalDateTime startDate, LocalDateTime endDate) {
    return scoreRepository.findByUserAndScoreDateBetween(user, startDate, endDate);
}

    public List<RankedUserScore> getRankedScores(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = scoreRepository.findRankedScores(startDate, endDate);
        List<RankedUserScore> rankedScores = new ArrayList<>();
        int rank = 1; // 순위 초기화

        for (Object[] result : results) {
            User user = (User) result[0];
            Long totalScore = (Long) result[1];
            rankedScores.add(new RankedUserScore(user, totalScore, rank)); // rank 추가
            rank++; // 다음 순위로 증가
        }

        return rankedScores;
    }
}
