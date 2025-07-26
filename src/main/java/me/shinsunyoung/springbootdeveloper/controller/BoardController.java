package me.shinsunyoung.springbootdeveloper.controller;


import jakarta.servlet.http.HttpSession;
import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.Respond;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import me.shinsunyoung.springbootdeveloper.service.RespondService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
public class BoardController {



    @Autowired
    private BoardService boardService;

    @Autowired
    private RespondService respondService;

    @Autowired
    private ScoreService scoreService;


    @GetMapping("/board/write")  //localhost:8080/board/write
    public String boardWriteForm(Model model, Principal principal, @AuthenticationPrincipal User username, HttpSession session) {
        //로그인시 닉네임 보이기
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }




        if (principal != null) {
            // 세션에 닉네임 저장
            String nickname = username.getNickname();
            System.out.println("닉네임: " + nickname); // 디버그용 로그
            session.setAttribute("nickname", nickname);
        } else {
            System.out.println("로그인하지 않은 상태입니다."); // 디버그용 로그
        }

        // 로그아웃 상태일 때 세션에서 닉네임 가져오기
        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname != null ? nickname : "익명"); // 닉네임이 없으면 "익명" 설정

        return "board/boardwrite";
    }



    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, Principal principal, @AuthenticationPrincipal User username) {
        // 닉네임이 존재할 경우 Board 객체에 설정
        if (principal != null && username != null) {
            String nickname = username.getNickname();
            board.setUserNickname(nickname); // 닉네임을 Board 객체에 설정
            board.setUser(username); // 로그인한 사용자 설정
        } else {
            // 닉네임이 없는 경우 처리 (예: 오류 메시지 추가)
            model.addAttribute("message", "닉네임이 확인되지 않았습니다.");
            model.addAttribute("sUrl", "/board/write"); // 다시 작성 페이지로 리다이렉트
            return "board/message";
        }

        // 게시글 작성 서비스 호출
        try {
            boardService.write(board); // 게시글을 데이터베이스에 저장
            model.addAttribute("message", "글 작성이 완료되었습니다.");
            model.addAttribute("sUrl", "/board/list");
        } catch (Exception e) {
            // 예외 발생 시 오류 메시지 추가
            model.addAttribute("message", "글 작성 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("sUrl", "/board/write"); // 다시 작성 페이지로 리다이렉트
        }

        log.info("체크");
        return "board/message";
    }




    //관리자가 일대일 문의 보기
    @GetMapping("/adminBoard/list")
    public String boardList1(Model model,
                             @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
                             String searchKeyword) {


        // 검색 했을 때 구분 위한 null
        Page<Board> list;

        if (searchKeyword == null) {
            // 검색 x: 기존 동일하게 모든 데이터 불러오기
            list = boardService.boardList(pageable);
        } else {
            // 검색한 데이터만 불러오기
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        // 페이지 계산 로직
        int firstPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(firstPage - 4, 1);
        int lastPage = Math.min(firstPage + 5, list.getTotalPages());

        // 페이지 리스트
        model.addAttribute("list", list);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);

        // 각 Board 객체에서 작성자의 닉네임을 가져와서 모델에 추가
        model.addAttribute("boards", list.getContent().stream()
                .map(board -> {
                    // 작성자의 닉네임을 Board 객체에 설정 (로그인 상태와 무관)
                    board.setUserNickname(board.getUserNickname()); // 닉네임을 Board 객체에 설정
                    return board;
                })
                .collect(Collectors.toList()));

        return "admin/adminBoardList";
    }


    //고객이 1대1문의 리스트 호출 메서드
    @GetMapping("/board/list")
    public String boardList(Model model, Principal principal, @AuthenticationPrincipal User username,
                            @PageableDefault(page = 0, size = 5, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        //로그인시 닉네임 보이기
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }


        // 로그인한 사용자의 닉네임을 모델에 추가
        if (principal != null) {
            User loggedInUser = (User) ((Authentication) principal).getPrincipal(); // 로그인한 사용자 정보 가져오기
            model.addAttribute("nickname", loggedInUser.getNickname());
            model.addAttribute("loggedInUserId", loggedInUser.getUserId()); // 사용자 ID 추가
        }

        // 검색 했을 때 구분 위한 null
        Page<Board> list;

        if (searchKeyword == null) {
            // 검색 x: 기존 동일하게 모든 데이터 불러오기
            list = boardService.boardList(pageable);
        } else {
            // 검색한 데이터만 불러오기
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        // 페이지 계산 로직
        int firstPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(firstPage - 4, 1);
        int lastPage = Math.min(firstPage + 5, list.getTotalPages());

        // 페이지 리스트
        model.addAttribute("list", list);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);

        // 각 Board 객체에서 작성자의 닉네임을 가져와서 모델에 추가
        model.addAttribute("boards", list.getContent().stream()
                .map(board -> {
                    board.setUserNickname(board.getUserNickname()); // 닉네임을 Board 객체에 설정
                    return board;
                })
                .collect(Collectors.toList()));

        model.addAttribute("principal", principal); // principal 객체 추가

        return "board/boardlist";
    }




    //자신이 쓴 일대일문의만 보게 하기
    @GetMapping("/board/view")   // localhost:8080/board/view?boardId=1
    public String boardView(Model model, Long boardId, Principal principal, @AuthenticationPrincipal User username) {

        // 현재 로그인한 사용자의 닉네임을 모델에 추가
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }


        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }


        // 게시글 조회
        Board board = boardService.boardView(boardId);

        // 게시글이 존재하는지 확인
        if (board == null) {
            return "redirect:/board/list"; // 게시글이 없으면 리스트로 리다이렉트
        }

        // 게시글 작성자와 현재 로그인한 사용자의 닉네임 비교
        if (!board.getUserNickname().equals(username.getNickname())) {
            model.addAttribute("errorMessage", "해당 게시글을 볼 자격이 없습니다.");
            return "board/forbidden"; // 에러 페이지로 이동
        }

        List<Respond> responds = respondService.getRespondsByBoard(board);
        model.addAttribute("responds", responds);

        // 게시글을 모델에 추가
        model.addAttribute("board", board);

        return "board/boardview"; // 게시글 보기 페이지
    }




    //관리자가  고객의 일대일 문의 보기
    @GetMapping("/adminboard/view")   //localhost:8080/board/view ?  boardId = 1
    public String adminboardView(Model model,Long boardId,  Principal principal, @AuthenticationPrincipal User username) {

        Board board = boardService.boardView(boardId);

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        List<Respond> responds = respondService.getRespondsByBoard(board);
        model.addAttribute("responds", responds);
        model.addAttribute("board", boardService.boardView(boardId));

        return "admin/adminBoardview";
    }



    @GetMapping("/board/delete")
    public String boardDelete(Long boardId, Long respondId, Model model, Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        // respondId가 있는 경우, 해당 게시글 삭제
        if (respondId != null) {
            boardService.respondDelete(respondId); // respondId에 해당하는 댓글 삭제
            model.addAttribute("message", "댓글 삭제 완료.");
        } else {
            boardService.boardDelete(boardId); // boardId에 해당하는 게시글 삭제
            model.addAttribute("message", "글 삭제 완료.");
        }

        model.addAttribute("sUrl","/board/list");

        return "board/message";
    }




    //게시판 문의 수정페이지 불러오기
    @GetMapping("/board/modify/{boardId}")
    public String boardModify(@PathVariable("boardId") Long boardId, Model model,  Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
        }

        model.addAttribute("board", boardService.boardView(boardId));

        return "board/boardmodify";
    }

//게시판 문의 수정정보 업데이트
@PostMapping("/board/update/{boardId}")
public String boardUpdate(@PathVariable("boardId") Long boardId, Board board, Model model, Principal principal, @AuthenticationPrincipal User username) {

    if (principal != null) {
        model.addAttribute("nickname", username.getNickname());
    }

    Board boardTemp = boardService.boardView(boardId);

    boardTemp.setBoardTitle(board.getBoardTitle());
    boardTemp.setBoardContent(board.getBoardContent());

    boardService.write(boardTemp);

    model.addAttribute("message", "글 수정이 완료되었습니다.");
    model.addAttribute("sUrl", "/board/list"); // 절대 경로로 설정

    return "board/message";
}






}
