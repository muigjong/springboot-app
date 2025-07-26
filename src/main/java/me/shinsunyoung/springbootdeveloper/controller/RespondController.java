package me.shinsunyoung.springbootdeveloper.controller;

import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.Respond;
import me.shinsunyoung.springbootdeveloper.service.BoardService;
import me.shinsunyoung.springbootdeveloper.service.RespondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RespondController {

    @Autowired
    private RespondService respondService;

    @Autowired
    private BoardService boardService; // 게시판 서비스

    //게시판 문의 답글 다는 페이지 불러오기
    @GetMapping("/respondForm")
    public String respondForm(@RequestParam Long boardId, Model model) {
        Board board = boardService.boardView(boardId); // boardService의 boardView 메소드 사용
        model.addAttribute("board", board);
        return "admin/adminrespondForm"; // 답글 작성 뷰
    }


//    게시판 문의 답글 업로드하기
    @PostMapping("/respond")
    public String createRespond(Respond respond, @RequestParam Long boardId) {
        // boardId가 null이거나 유효하지 않을 경우 예외 처리
        if (boardId == null) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다."); // 또는 다른 예외 처리
        }

        Board board = boardService.boardView(boardId); // boardService의 boardView 메소드 사용
        respond.setBoard(board);
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



