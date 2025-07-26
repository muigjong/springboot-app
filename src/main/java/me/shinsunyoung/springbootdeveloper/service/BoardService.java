package me.shinsunyoung.springbootdeveloper.service;

import jakarta.persistence.EntityNotFoundException;
import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.exception.ResourceNotFoundException;
import me.shinsunyoung.springbootdeveloper.repository.BoardRepository;
import me.shinsunyoung.springbootdeveloper.repository.RespondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private RespondRepository respondRepository; // RespondRepository 주입 추가

    // 게시글 작성
    public void write(Board board) {
        boardRepository.save(board);
    }

    // 게시글 리스트
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 제목 또는 닉네임으로 문의글 검색
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByBoardTitleContainingOrUser_NicknameContaining(searchKeyword, searchKeyword, pageable);
    }

    // 게시글 보기
    public Board boardView(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("없는 ID입니다."));
    }

    // 댓글 삭제
    public void respondDelete(Long respondId) {
        if (respondId == null) {
            throw new IllegalArgumentException("respondId는 null일 수 없습니다.");
        }

        // 존재 여부 체크
        if (!respondRepository.existsById(respondId.intValue())) {
            throw new EntityNotFoundException("해당 ID의 댓글이 존재하지 않습니다.");
        }

        // 댓글 삭제
        respondRepository.deleteById(respondId.intValue());
    }

    // 글 삭제
    public void boardDelete(Long boardId) {
        if (boardId == null) {
            throw new IllegalArgumentException("boardId는 null일 수 없습니다.");
        }

        boardRepository.deleteById(boardId);
    }

    public List<Board> getBoardsByUser(User user) {
        return boardRepository.findByUser(user); // User 객체를 직접 사용
    }

    // 게시판 ID로 게시판 정보 조회
    public Board findById(Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            return boardOptional.get();
        } else {
            throw new ResourceNotFoundException("게시판을 찾을 수 없습니다.");
        }
    }
}
