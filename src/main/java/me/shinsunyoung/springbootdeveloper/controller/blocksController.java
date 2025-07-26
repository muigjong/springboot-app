package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class blocksController {

    @GetMapping("/games/blocks")
    public String blocks(Model model, Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        return "games/blocks"; // blocks.html은 src/main/resources/templates 폴더에 위치
    }
}
