package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.Event;
import me.shinsunyoung.springbootdeveloper.domain.Score;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.RankedUserScore;
import me.shinsunyoung.springbootdeveloper.service.EventService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/admin/events")
    public String listEvents(Model model) {
        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "admin/eventList"; // 이벤트 리스트 뷰
    }

    @GetMapping("/admin/events/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/createEvent"; // 이벤트 생성 뷰
    }

    @PostMapping("/admin/events/create")
    public String createEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }


//    사용자가 이벤트 순위 보기
@GetMapping("/users/rankings")
public String viewRankings(Model model, Principal principal, @AuthenticationPrincipal User username) {
    if (principal != null) {
        model.addAttribute("nickname", username.getNickname());

        // 로그인한 사용자의 총 점수를 가져오는 로직 추가
        Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
        model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
    }

        // 현재 활성화된 이벤트를 가져옵니다.
    Event currentEvent = eventService.getCurrentEvent();

    // 현재 이벤트가 null인 경우
    if (currentEvent == null) {
        model.addAttribute("error", "현재 이벤트가 없습니다. 이벤트가 시작되면 공지사항을 통해 알려드립니다.");
        return "users/rankings"; // 오류 메시지를 포함한 사용자 순위 리스트 뷰
    }

    // 이벤트 기간을 설정합니다.
    LocalDateTime startDateTime = currentEvent.getStartDate().atStartOfDay();
    LocalDateTime endDateTime = currentEvent.getEndDate().atTime(23, 59, 59);

    // 사용자 순위를 가져옵니다.
    List<RankedUserScore> rankedScores = scoreService.getRankedScores(startDateTime, endDateTime);
    model.addAttribute("rankedScores", rankedScores);



    return "users/rankings"; // 사용자 순위 리스트 뷰
}


}

