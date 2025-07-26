package me.shinsunyoung.springbootdeveloper.service;

import me.shinsunyoung.springbootdeveloper.domain.FAQ;
import me.shinsunyoung.springbootdeveloper.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FAQService {

    @Autowired
    private FAQRepository faqRepository;

    // FAQ 목록 조회
    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }

    // FAQ 생성
    public FAQ createFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }

    // FAQ 조회
    public Optional<FAQ> getFAQById(Long id) {
        return faqRepository.findById(id);
    }

    // FAQ 수정
    public FAQ updateFAQ(Long id, FAQ faqDetails) {
        FAQ faq = faqRepository.findById(id).orElseThrow(() -> new RuntimeException("FAQ not found"));
        faq.setFaqTitle(faqDetails.getFaqTitle());
        faq.setFaqText(faqDetails.getFaqText());
        return faqRepository.save(faq);
    }

    // FAQ 삭제
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }
}

