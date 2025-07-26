package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.Notice;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.service.NoticeService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ScoreService scoreService;

    private final String uploadDir = "C:/Apache24/htdocs/fileServer/images1/notice/";

    //관리자가 공지사항입력 페이지 호출하기
    @GetMapping("/notice/adminnoticeUpload")
    public String showAdminNoticeUploadForm() {
        return "notice/adminnoticeUpload"; // "adminnoticeUpload.html" 파일을 불러옵니다.
    }


    //관리자가 입력한 공지사항 데이터베이스에 올리기
    @PostMapping("/notice/upload")
    public String uploadNotice(
            @RequestParam("noticeTitle") String title,
            @RequestParam("noticeImage") MultipartFile image,
            @RequestParam("noticeText") String text,
            Model model) {

        try {
            if (!image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                if (fileName != null) {
                    File destinationDir = new File(uploadDir);
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    File destinationFile = new File(uploadDir + fileName);
                    image.transferTo(destinationFile);

                    // saveNotice 호출
                    noticeService.saveNotice(title, "fileServer/images1/notice/" + fileName, text);
                } else {
                    model.addAttribute("message", "유효한 파일 이름이 아닙니다.");
                }
            } else {
                model.addAttribute("message", "이미지를 선택해 주세요.");
            }

            model.addAttribute("message", "공지사항이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "공지사항 등록 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "notice/adminnoticeUpload"; // 다시 폼으로 돌아갑니다.
    }


    //관리자가 공지사항 목록 불러오기
    @GetMapping("/notice/list")
    public String listNotices(Model model) {
        List<Notice> notices = noticeService.getAllNotices(); // 공지사항 목록을 가져오는 서비스 메서드
        model.addAttribute("notices", notices);
        return "notice/noticelist"; // noticeList.html로 이동
    }


    // 관리자가 공지사항 수정 폼 호출
    @GetMapping("/notice/edit/{id}")
    public String showEditNoticeForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id); // 공지사항 ID로 조회
        model.addAttribute("notice", notice);
        return "notice/editNotice"; // editNotice.html 파일을 불러옵니다.
    }

    // 관리자가 수정된 공지사항 데이터베이스에 저장하기
    @PostMapping("/notice/update")
    public String updateNotice(
            @RequestParam("id") Long id,
            @RequestParam("noticeTitle") String title,
            @RequestParam("noticeImage") MultipartFile image,
            @RequestParam("noticeText") String text,
            Model model) {

        try {
            String imagePath = null;
            if (!image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                if (fileName != null) {
                    File destinationDir = new File(uploadDir);
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    File destinationFile = new File(uploadDir + fileName);
                    image.transferTo(destinationFile);
                    imagePath = "fileServer/images1/notice/" + fileName;
                }
            }

            // 기존 공지사항 정보 업데이트
            noticeService.updateNotice(id, title, imagePath, text);

            model.addAttribute("message", "공지사항이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "공지사항 수정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/notice/list"; // 목록으로 리다이렉트
    }

    // 관리자가 공지사항 삭제
    @PostMapping("/notice/delete/{id}")
    public String deleteNotice(@PathVariable Long id, Model model) {
        try {
            noticeService.deleteNotice(id); // 공지사항 삭제
            model.addAttribute("message", "공지사항이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "공지사항 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/notice/list"; // 목록으로 리다이렉트
    }


    //사용자가 공지사항 보기
    @GetMapping("/notice/customerCenter_notice") // "/notices" 경로로 GET 요청을 처리합니다.
    public String getNotices(Model model, Principal principal, @AuthenticationPrincipal User username) {
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


        List<Notice> notices = noticeService.getAllNotices(); // 모든 공지사항을 가져옵니다.
        model.addAttribute("notices", notices); // 모델에 공지사항 리스트를 추가합니다.
        return "notice/customerCenter_notice";
    }


}
