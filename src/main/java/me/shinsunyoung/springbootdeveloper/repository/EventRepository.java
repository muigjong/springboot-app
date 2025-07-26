package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {
            Event findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
    }

