package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.PageClick;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.PageClickRepository;
import me.shinsunyoung.springbootdeveloper.service.PageClickService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class mainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private PageClickService pageClickService;


    @Autowired
    private PageClickRepository pageClickRepository;





    @GetMapping("/coramdeobiblequiz")
    public String index(Model model, Principal principal, @AuthenticationPrincipal User username) {
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username);
            model.addAttribute("totalScore", totalScore);
        }

        // 페이지 클릭 기록 추가
        pageClickService.recordPageClick("main/home");

        // 클릭 횟수 가져오기
        PageClick pageClick = pageClickRepository.findByPageName("main/home").orElse(new PageClick("main/home"));
        Integer todayClicks = pageClick.getClickCounts().getOrDefault(LocalDate.now(), 0);
        Integer yesterdayClicks = pageClick.getClickCounts().getOrDefault(LocalDate.now().minusDays(1), 0);

        // 누적 방문 수 계산 (이전 날짜의 클릭 수를 모두 합산)
        Integer totalClicks = pageClick.getClickCounts().values().stream().mapToInt(Integer::intValue).sum();

        // 모델에 클릭 횟수 및 누적 방문 수 추가
        model.addAttribute("todayClicks", todayClicks);
        model.addAttribute("yesterdayClicks", yesterdayClicks);
        model.addAttribute("totalClicks", totalClicks); // 누적 방문 수 추가

        // 회원 수 추가
        Long userCount = userService.countUsers();
        model.addAttribute("userCount", userCount);

        return "main/home";
    }
}
