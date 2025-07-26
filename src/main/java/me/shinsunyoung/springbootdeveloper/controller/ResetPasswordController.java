package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

//비밀번호 재설정 - 아직 구현 못함, newPassword를 인식하지 못함
@Controller
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    public ResetPasswordController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/login/resetpassword")
    public String showResetPasswordForm() {
        return "login/resetpassword";
    }

    @PostMapping("/login/successresetpassword")
    public String resetPassword(@RequestParam String email, @RequestParam String memPhone, @RequestParam String newPassword, Model model) {
        boolean success = userService.resetPassword(email, memPhone, newPassword);

//        if (success) {
//            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
//        } else {
//            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
//        }

        return "login/successresetpassword";
    }



//    -------------------------------------------------------------------------------

//    //비밀번호 재설정위한 아이디와 이메일 입력 페이지 호출
//    @GetMapping("/login/resetpassword")
//    public String resetPassword(Model model) {
//        // 필요한 경우, 모델에 추가할 데이터를 설정할 수 있습니다.
//        return "login/resetpassword";
//    }
//
//
//
//    //로그인시 비밀번호 재설정 위한 아이디와 이메일주소 입력
//    @PostMapping("/login/resetpassword")
//    public String resetPassword(@RequestParam String memId, @RequestParam String email, Model model) {
//
//        boolean isUserValid = userService.verifyUser(memId, email);
//        if (isUserValid) {
//            model.addAttribute("username", memId);
//            return "login/newpassword";
//        } else {
//            model.addAttribute("error", "아이디 또는 이메일 주소가 올바르지 않습니다.");
//            return "login/resetpassword";
//        }
//    }
//
//    //새로운 비밀번호 입력페이지 호출
//    @GetMapping("/login/newpassword")
//    public String newPassword(Model model) {
//        return "login/newpassword";
//    }
//
////비밀번호 변경성공
//        @PostMapping("/login/newpassword")
//        public String setNewPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, Model model) {
//            userService.updatePassword(email, newPassword);
//            boolean success = userService.resetPassword(email, newPassword);
//
//            if (success) {
//                model.addAttribute("email", email);
//                model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
//                return "redirect:/successresetpassword?email=" + email;
//            } else {
//                model.addAttribute("error", "해당 이메일을 가진 사용자를 찾을 수 없습니다.");
//                return "login/newpassword";
//            }
//
//              }
//
//
//
//
//
//
    //비밀번호재설정 성공했다는 메시지 호출
    @GetMapping("/login/successresetpassword")
    public String resetPasswordSuccess(@RequestParam String nickname, Model model) {

        model.addAttribute("nickname", nickname);
        return "login/successresetpassword";
    }
}
//
//
//
//
//
//
