package me.shinsunyoung.springbootdeveloper.repository;


import me.shinsunyoung.springbootdeveloper.domain.QuizQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    //전체회원 출력시 페이징 처리하면서 댓글단 사용자 검색
    //이메일검색
    Page<QuizQuestion> findBySortContaining(String sort, Pageable pageable);
}

