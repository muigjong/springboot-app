package me.shinsunyoung.springbootdeveloper.service;

import me.shinsunyoung.springbootdeveloper.domain.Event;
import me.shinsunyoung.springbootdeveloper.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public Event getCurrentEvent() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }
}
