package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "event_tb")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate;   // 종료 날짜

    // 기본 생성자
    public Event() {}

    // 생성자
    public Event(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters 및 Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

