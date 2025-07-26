package me.shinsunyoung.springbootdeveloper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminController {

    //관리자 페이지 바로가기
    @GetMapping("/admin/home")
    public String login() {
        return "admin/adminMain";
    }
}

