package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class FindEmailController {

    private final UserRepository userRepository;

    @Autowired
    public FindEmailController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //로그인시 이메일찾기 뷰 호출
    @GetMapping("/login/findemail")
    public String findEmail(Model model) {
        return "login/findemail";
    }

    //로그인시 이메일 찾기
    @PostMapping("/login/findemail")
    public String findEmail2(@RequestParam String nickname, @RequestParam String phone, Model model) {
        Optional<User> user = userRepository.findByNicknameAndPhone(nickname, phone);
        if (user.isPresent()) {
            model.addAttribute("email", user.get().getEmail());
            return "login/foundemail";
        } else {
            model.addAttribute("error", "일치하는 사용자가 없습니다.");
            return "login/findemail";
        }
    }

    //이메일 주소 찾았을 때 찾았다고 알리는 뷰 호출
    @GetMapping("/login/foundemail")
    public String foundEmail(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "login/foundemail";
    }
}
