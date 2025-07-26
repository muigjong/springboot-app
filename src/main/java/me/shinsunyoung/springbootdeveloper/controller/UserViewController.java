package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping
public class UserViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreService scoreService;



    //로그인 호출
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //관리자가 전체 회원 목록 조회
    @GetMapping("/admin/AllUsers")
    public String allUsers(Model model, @PageableDefault(page = 0, size = 5, sort = "userId",
            direction = Sort.Direction.DESC)
//                            내림차순 : Sort.Direction.DESC 오름차순 : Sort.Direction.ASC String 값 : Sort.Direction.fromString( String 문자열 )
    Pageable pageable, String searchKeyword1, String searchKeyword2, String searchKeyword3 ) {

////1. 모든 데이터 가져오기
//        List<User> user = userRepository.findAll();


        // 기존 Page(Board> list = boardService.boardList(pageable);
        // 검색 했을대 안했을때 구분 위한 null
        Page<User> useralllist = null;

        //이메일 검색(서비스에서 이메일과 연결)
        if (searchKeyword1 == null) {
            useralllist = userService.userList(pageable);
        } else {
            useralllist = userService.userSearchList1(searchKeyword1, pageable);
        }





        //+1 <-- pageable 은 0부터 시작 +1 추가 1페이지부터시작
        int firstPage = useralllist.getPageable().getPageNumber() + 1;
        //현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수
        //-1 <-- 값이 들어가는것을 막기위해 max 값 으로 두개 값을 넣고 더 큰 값을 넣어줌
        //.max =  현제 페이지에서 -4해줬을때 1보다 작은수가나오면 안되서 1혹은 현제 페이지 -4했을때 보다 큰값을 쓰기 위함
        int startPage = Math.max(firstPage - 4, 1);
        //현재 페이지에서 가장 뒤에 페이지 번호를 보여줄 변수
        //.min = 해당 번호가 넘어가버리면 안되기때문에 최소값을 구하는 방식
        int lastPage = Math.min(firstPage + 5, useralllist.getTotalPages());

        model.addAttribute("users", useralllist);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);

        return "admin/adminAllUsers";
    }


    //관리자가 개별회원 상세보기
    @GetMapping("/admin/IdUser")
    public String iduserView(Model model, Long id) {

        model.addAttribute("user", userService.iduserView(id));

        return "admin/adminIdUser";
    }





    //관리자가 회원 강퇴
    @GetMapping("/admin/userDelete")
    public String userDelete(Long id, Model model) {

        userService.userDelete(id);

        model.addAttribute("message", "회원강퇴 완료");

        return "redirect:/admin/AllUsers";

    }

    //아래는 회원관련 ------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------//
    //회원이 이메일로 로그인한 후 마이페이지에서 자신의 회원정보를 가져오기
    @GetMapping("/users/view")
    public String userView(Model model, Principal principal, @AuthenticationPrincipal User username) {
        // 현재 로그인한 사용자의 이메일을 가져오기
        String email = principal.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "users/IdUser"; // 회원정보 가져오기
        } else {
            model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
            return "main/home"; //실패시 메인홈이로 이동
        }
    }


    //회원이 자신의 정보를 수정하기 위해 페이지 불러오기
    @GetMapping("/users/modify")
    public String showUserUpdateForm(Principal principal, Model model, @AuthenticationPrincipal User username) {
        String email = principal.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "users/modifyUser"; // 수정페이지 가져오기
        } else {
            model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
            return "main/home";  //실패시 메인페이지로 이동
        }


    }


    //회원이 자신의 정보를 수정해서 데이터베이스로 보내기
    @PostMapping("/users/update")
    public String userUpdate(Principal principal, User user, Model model) {
        String email = principal.getName();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User userModify = userOptional.get();

            // 사용자 정보를 업데이트합니다.
            userModify.setBirthday(user.getBirthday());
            userModify.setChurch(user.getChurch());
            userModify.setPhone(user.getPhone());
            userService.save(userModify);

            model.addAttribute("message", "회원 정보 수정이 완료되었습니다.");
            model.addAttribute("sUrl", "/users/view");  //수정완료시 업데이트된 회원정보보기 페이지로 이동
            model.addAttribute("user", userModify); // 수정된 사용자 객체를 모델에 추가
        } else {
            model.addAttribute("message", "사용자 정보를 찾을 수 없습니다.");
            return "/users/view";  //실패시 기존 정보가 담긴 회원정보보기 페이지로 이동
        }

        return "redirect:/users/view";  //회원정보업데이트하기
    }

    //회원 탈퇴
    @GetMapping("/users/userDelete")
    public String userCancel(Long id, Model model) {

        userService.userDelete(id);

        model.addAttribute("message", "회원탈퇴 완료");

        return "redirect:/logout";

    }










}


