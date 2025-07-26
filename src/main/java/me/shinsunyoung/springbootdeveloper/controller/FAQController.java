package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.FAQ;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.service.FAQService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class FAQController {

    @Autowired
    private FAQService faqService;

    @Autowired
    private ScoreService scoreService;

    // FAQ 목록 페이지
    @GetMapping("/faqList")
    public String getAllFAQs(Model model) {
        List<FAQ> faqs = faqService.getAllFAQs();
        model.addAttribute("faqs", faqs);
        return "faq/faqList"; // faq_list.html
    }

    // FAQ 추가 페이지
    @GetMapping("/faqAdd")
    public String showAddFAQForm(Model model) {
        model.addAttribute("faq", new FAQ());
        return "faq/faqAdd"; // faq_form.html
    }

    // FAQ 등록 처리
    @PostMapping("/faqUpoad")
    public String addFAQ(FAQ faq) {
        faqService.createFAQ(faq);
        return "redirect:/faqList"; // 등록 후 목록으로 리다이렉트
    }

    // FAQ 수정 페이지
    @GetMapping("/faq/edit/{id}")
    public String showEditFAQForm(@PathVariable Long id, Model model) {
        FAQ faq = faqService.getFAQById(id).orElseThrow(() -> new RuntimeException("FAQ not found"));
        model.addAttribute("faq", faq);
        return "faq/faqEdit"; // faq_edit.html
    }

    // FAQ 수정 처리
    @PostMapping("/faq/edit/{id}")
    public String updateFAQ(@PathVariable Long id, @ModelAttribute FAQ faqDetails) {
        faqService.updateFAQ(id, faqDetails);
        return "redirect:/faqList"; // 수정 후 목록으로 리다이렉트
    }

// 관리자가 faq 삭제하기
    @PostMapping("/faq/delete/{id}")
    public String deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return "redirect:/faqList"; // 삭제 후 목록으로 리다이렉트 (faqList로 수정)
    }


// 사용자가 faq 보기
@GetMapping("/faqUserView")
public String showFAQList(Model model, Principal principal, @AuthenticationPrincipal User username) {

        //로그인시 닉네임 보이기
    if (principal != null) {
        model.addAttribute("nickname", username.getNickname());
    }

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


    // FAQ 목록을 가져옵니다.
    List<FAQ> faqs = faqService.getAllFAQs();
    model.addAttribute("faqs", faqs);
    return "faq/faquserview"; // FAQ 목록을 보여줄 HTML 파일 이름
}

}

