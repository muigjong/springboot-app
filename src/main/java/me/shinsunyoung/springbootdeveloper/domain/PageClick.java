package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class PageClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pageName;

    @ElementCollection
    @CollectionTable(name = "click_counts", joinColumns = @JoinColumn(name = "page_click_id"))
    @MapKeyColumn(name = "click_date")
    @Column(name = "click_count")
    private Map<LocalDate, Integer> clickCounts = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "page_clicks", joinColumns = @JoinColumn(name = "page_click_id"))
    @MapKeyColumn(name = "page_name")
    @Column(name = "click_count")
    private Map<String, Integer> pageClickCounts = new HashMap<>();

    // 기본 생성자
    public PageClick() {
    }

    // 매개변수가 있는 생성자
    public PageClick(String pageName) {
        this.pageName = pageName;
        this.clickCounts = new HashMap<>(); // 클릭 카운트 초기화
        this.pageClickCounts = new HashMap<>(); // 페이지 클릭 카운트 초기화
    }

    // 클릭 횟수를 증가시키는 메서드
    public void incrementClickCount(LocalDate date) {
        clickCounts.put(date, clickCounts.getOrDefault(date, 0) + 1);
    }

    // 페이지 이름에 따른 클릭 횟수를 증가시키는 메서드
    public void incrementPageClickCount(String pageName) {
        pageClickCounts.put(pageName, pageClickCounts.getOrDefault(pageName, 0) + 1);
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Map<LocalDate, Integer> getClickCounts() {
        return clickCounts;
    }

    public void setClickCounts(Map<LocalDate, Integer> clickCounts) {
        this.clickCounts = clickCounts;
    }

    public Map<String, Integer> getPageClickCounts() {
        return pageClickCounts;
    }

    public void setPageClickCounts(Map<String, Integer> pageClickCounts) {
        this.pageClickCounts = pageClickCounts;
    }
}
