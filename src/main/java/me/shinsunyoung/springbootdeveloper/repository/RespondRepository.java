package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.Respond;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespondRepository extends JpaRepository<Respond, Integer> {
    List<Respond> findByBoard(Board board);

    // 댓글 존재 여부 확인
    boolean existsById(Integer respondId);
}

