package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    @Column
    private String faqTitle;

    @Column
    private String faqText;

    @Column
    private LocalDateTime createdAt; // 공지사항 올린 시간 필드 추가

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // 공지사항 생성 시 현재 시간 저장
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "등록되지 않음"; // null 처리
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createdAt.format(formatter);
    }

}
