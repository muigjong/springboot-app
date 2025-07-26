package me.shinsunyoung.springbootdeveloper.service;

import me.shinsunyoung.springbootdeveloper.domain.PageClick;
import me.shinsunyoung.springbootdeveloper.repository.PageClickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PageClickService {

    @Autowired
    private PageClickRepository pageClickRepository;

    public void recordPageClick(String pageName) {
        LocalDate today = LocalDate.now();

        // 페이지 클릭 정보를 조회하거나 새로운 엔티티 생성
        PageClick pageClick = pageClickRepository.findByPageName(pageName)
                .orElse(new PageClick(pageName));

        // 클릭 횟수 증가
        pageClick.incrementClickCount(today);

        // 페이지 이름에 따른 클릭 횟수 증가
        pageClick.incrementPageClickCount(pageName);

        // 엔티티 저장
        pageClickRepository.save(pageClick);
    }
}