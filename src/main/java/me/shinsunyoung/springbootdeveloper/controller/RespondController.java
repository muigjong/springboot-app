package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.Respond;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.service.BoardService;
import me.shinsunyoung.springbootdeveloper.service.RespondService;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RespondController {

    @Autowired
    private RespondService respondService;

    @Autowired
    private BoardService boardService; // 게시판 서비스

    @Autowired //
    private UserService userService;

    //게시판 문의 답글 다는 페이지 불러오기
    @GetMapping("/respondForm")
    public String respondForm(@RequestParam Long boardId, Model model) {
        Board board = boardService.boardView(boardId); // boardService의 boardView 메소드 사용
        model.addAttribute("board", board);
        return "admin/adminrespondForm"; // 답글 작성 뷰
    }


//    게시판 문의 답글 업로드하기
//    @PostMapping("/respond")
//    public String createRespond(Respond respond, @RequestParam Long boardId) {
//        // boardId가 null이거나 유효하지 않을 경우 예외 처리
//        if (boardId == null) {
//            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다."); // 또는 다른 예외 처리
//        }
//
//        Board board = boardService.boardView(boardId); // boardService의 boardView 메소드 사용
//        respond.setBoard(board);
//        respondService.saveRespond(respond);
//
//        return "redirect:/adminboard/view?boardId=" + boardId; // 게시글 상세 페이지로 리다이렉트
//    }

    @PostMapping("/respond")
    public String createRespond(Respond respond, @RequestParam Long boardId) {
        // boardId가 null이거나 유효하지 않을 경우 예외 처리
        if (boardId == null) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        // 1. 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("로그인이 필요합니다. 댓글을 작성하려면 로그인해주세요.");
        }

        // 인증 객체에서 Principal(사용자 정보) 가져오기
        // *************************************************************
        // *** 이 부분이 변경된 핵심입니다! Principal에서 직접 user ID를 가져옴 ***
        // *************************************************************
        Long currentUserId = null;
        Object principal = authentication.getPrincipal();

        // Principal이 User 엔티티 타입(또는 Custom UserDetails)이며, getId() 메서드를 제공한다고 가정
        if (principal instanceof User) { // <- 본인의 User 엔티티 클래스 타입으로 변경
            currentUserId = ((User) principal).getUserId(); // User 엔티티에 getId() 메서드가 반드시 있어야 합니다!
        } else {
            // UserDetails 인터페이스만 구현했거나, Principal이 ID를 직접 노출하지 않는 경우
            // 다른 방법으로 ID를 가져와야 합니다. 여기서는 에러로 처리
            throw new IllegalStateException("현재 로그인된 사용자 ID를 principal에서 직접 가져올 수 없습니다. Principal 타입: " + principal.getClass().getName());
        }

        // user_id를 가져와서 User 엔티티를 찾기 (UserService의 findById 사용)
        // 예시: User currentUser = userService.findById(currentUserId);
        User currentUser = userService.findById(currentUserId); // 이 메서드는 실제로 유저를 DB에서 찾아와야 합니다.

        if (currentUser == null) {
            // DB에서 사용자 정보를 찾지 못했다면 문제 발생!
            // (findById 메서드에서 이미 예외를 던지겠지만, 한 번 더 방어적으로 체크)
            throw new IllegalStateException("ID " + currentUserId + "에 해당하는 사용자 정보를 시스템에서 찾을 수 없습니다.");
        }

        // 2. Respond 객체에 현재 사용자 정보 설정
        respond.setUser(currentUser); // Respond 엔티티에 @ManyToOne User user; 필드가 있을 경우

        // 만약 Respond 엔티티에 private Long userId; 와 같이 ID만 저장한다면
        // respond.setUserId(currentUser.getId());
        // 이 두 방식 중 본인의 Respond 엔티티에 맞는 것으로 사용해야 합니다.

        // 게시글 정보 설정
        Board board = boardService.boardView(boardId);
        respond.setBoard(board);

        // 댓글 저장
        respondService.saveRespond(respond);

        return "redirect:/adminboard/view?boardId=" + boardId; // 게시글 상세 페이지로 리다이렉트
    }




//답글 수정뷰 호출하기
    @GetMapping("/responds/edit/{id}/{boardId}")
    public String editRespond(@PathVariable int id, @PathVariable Long boardId, Model model) {
        Respond respond = respondService.findById(id);
        model.addAttribute("respond", respond);
        model.addAttribute("boardId", boardId);
        return "admin/respondEdit"; // 수정 뷰로 이동
    }

    //답글 수정 하기
    @PostMapping("/responds/update")
    public String updateRespond(@RequestParam int id, @RequestParam String content, @RequestParam Long boardId) {
        respondService.updateRespond(id, content);
        return "redirect:/adminboard/view?boardId=" + boardId; // 수정 후 게시판으로 리다이렉트
    }






    // 답글 삭제 처리
    @PostMapping("/responds/delete/{id}")
    public String deleteRespond(@PathVariable int id, @RequestParam Long boardId) {
        respondService.deleteRespond(id);
        return "redirect:/adminboard/view?boardId=" + boardId; // 게시글 상세 페이지로 리다이렉트
    }
}

