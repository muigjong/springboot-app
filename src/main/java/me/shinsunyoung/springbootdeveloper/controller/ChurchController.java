package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.Score;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.UserScore;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ChurchController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/church")
    public String churchForm(Model model, Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        return "church/churchUsers"; // 조회 페이지로 이동
    }

    // 교회별 회원 점수 조회
    @GetMapping("/church/churchUsers")
    public String getScores(@RequestParam(name = "church") String church, Model model, Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        List<Score> scores = scoreService.getScoresByChurchOrdered(church);
        Long churchTotalScore = scoreService.getTotalScoreByChurch(church);
        List<UserScore> userScores = scoreService.getUniqueUserScores(scores);



        model.addAttribute("scores", scores);
        model.addAttribute("churchName", church);
        model.addAttribute("churchTotalScore", churchTotalScore);
        model.addAttribute("userScores", userScores != null ? userScores : List.of()); // null 체크 및 기본값 설정
        return "church/churchUsers";
    }


    //교회별 점수 순위
    @GetMapping("/church/churches")
    public String getChurchScoresRanked(Model model, Principal principal, @AuthenticationPrincipal User username) {
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }
        Map<String, Long> scores = scoreService.getTotalScoresByAllChurches();

        List<ChurchScore> churchScores = scores.entrySet().stream()
                .map(entry -> new ChurchScore(entry.getKey(), entry.getValue()))
                .sorted((c1, c2) -> Long.compare(c2.getScore(), c1.getScore()))
                .collect(Collectors.toList());

        model.addAttribute("churchScores", churchScores);
        return "church/churches"; // Thymeleaf 뷰 이름
    }

    public static class ChurchScore {
        private String churchName;
        private Long score;

        public ChurchScore(String churchName, Long score) {
            this.churchName = churchName;
            this.score = score;
        }

        public String getChurchName() {
            return churchName;
        }

        public Long getScore() {
            return score;
        }
    }




}
