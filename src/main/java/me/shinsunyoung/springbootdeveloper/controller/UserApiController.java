package me.shinsunyoung.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.AddUserRequest;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    //회원가입 페이지 호출
    @GetMapping("/join")
    public String signup() {
        return "join";
    }


    //회원가입 진행
    //이메일, 아이디, 닉네임, 연락처 중복 확인
    @Transactional
    @PostMapping("/user")
//    public String signup(, @Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model ) {
    public String signup(@Valid @ModelAttribute("user") AddUserRequest request, BindingResult bindingResult, Model model ) {



        userService.save(request);

        return "redirect:login";
    }


    //로그아웃 호출
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "main/main1";
    }


    //회원가입시 이메일 중복 확인
    @PostMapping("/api/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

// 회원가입시 닉네임 중복 확인
@PostMapping("/api/check-nickname")
public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> payload) {
    String nickname = payload.get("nickname");
    boolean isDuplicate = userService.isNicknameDuplicate(nickname);
    return ResponseEntity.ok(isDuplicate);
}


//회원 가입시 폰 번호 중복확인
@PostMapping("/api/check-phone")
public ResponseEntity<Boolean> checkPhone(@RequestBody Map<String, String> payload) {
    String phone = payload.get("phone");
    boolean isDuplicate = userService.isPhoneDuplicate(phone);
    return ResponseEntity.ok(isDuplicate);
}
}
