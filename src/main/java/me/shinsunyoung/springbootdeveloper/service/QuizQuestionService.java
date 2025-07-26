package me.shinsunyoung.springbootdeveloper.service;



import me.shinsunyoung.springbootdeveloper.domain.QuizQuestion;
import me.shinsunyoung.springbootdeveloper.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class QuizQuestionService {
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    // 문제 저장
    public void saveQuizQuestion(QuizQuestion quizQuestion) {
        quizQuestionRepository.save(quizQuestion);
    }

    // 모든 문제 조회
    public List<QuizQuestion> findAll() {
        return quizQuestionRepository.findAll();
    }

    // 랜덤으로 문제 조회
    public List<QuizQuestion> findRandomQuestions() {
        List<QuizQuestion> allQuestions = findAll();
        Collections.shuffle(allQuestions); // 문제 섞기
        return allQuestions.stream().limit(4).toList(); // 4개 문제 반환
    }

    //모든 문제 리스트
    //List<user>값이 페이징 설정 해주면서 page<user>로 넘김
    public Page<QuizQuestion> quizQuestionList(Pageable pageable) {
        return quizQuestionRepository.findAll(pageable);
    }

    //페이징 처리시 댓글 적은 사용자 검색 기능(권별로 검색)
    public Page<QuizQuestion> quizQuestionSearchList1(String searchKeyword1, Pageable pageable) {
        return quizQuestionRepository.findBySortContaining(searchKeyword1, pageable);
    }
}


