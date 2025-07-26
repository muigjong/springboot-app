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

@Controller
public class policyController {

    @Autowired
    private ScoreService scoreService;




    @GetMapping("/policyInfor/policies")
    public String login(Model model, Principal principal, @AuthenticationPrincipal User username) {
        //로그인시 닉네임 보이기
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }




        return "policyInfor/policies";
    }

}
