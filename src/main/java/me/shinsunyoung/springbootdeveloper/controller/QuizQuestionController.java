package me.shinsunyoung.springbootdeveloper.controller;


import me.shinsunyoung.springbootdeveloper.domain.QuizQuestion;
import me.shinsunyoung.springbootdeveloper.domain.Score;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.service.QuizQuestionService;
import me.shinsunyoung.springbootdeveloper.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class QuizQuestionController {
    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private ScoreService scoreService;

    // 문제 생성 폼을 보여주는 메서드
    @GetMapping("/quiz/createQuizQuestion")
    public String showCreateForm(Model model) {
        model.addAttribute("quizQuestion", new QuizQuestion());
        return "quiz/createQuizQuestion"; // createQuizQuestion.html을 반환
    }

    // 문제 생성 폼 제출을 처리하는 메서드
    @PostMapping("/quiz/createQuizQuestion")
    public String createQuizQuestion(
            @RequestParam String question,
            @RequestParam String options, // String으로 받음
            @RequestParam String correctAnswer,
            @RequestParam String commentary, // commentary 추가
             @RequestParam String sort
    ) {

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestion(question);
        quizQuestion.setOptions(options); // 직접 setOptions 호출
        quizQuestion.setCorrectAnswer(correctAnswer);
        quizQuestion.setCommentary(commentary); // commentary 설정
        quizQuestion.setSort(sort);

        // 퀴즈 질문 저장
        quizQuestionService.saveQuizQuestion(quizQuestion);
        return "redirect:/quiz/listQuizQuestions";
    }







    // 문제 목록을 보여주는 메서드
    @GetMapping("/quiz/listQuizQuestions")
    public String listQuizQuestions(Model model, @PageableDefault(page = 0, size = 10, sort = "quizQuestionId",
            direction = Sort.Direction.DESC)
//                            내림차순 : Sort.Direction.DESC 오름차순 : Sort.Direction.ASC String 값 : Sort.Direction.fromString( String 문자열 )
    Pageable pageable, String searchKeyword1, String searchKeyword2, String searchKeyword3 ) {

        Page<QuizQuestion> quizQuestionalllist = null;

        //권별 검색(서비스에서 권별과 연결)
        if (searchKeyword1 == null) {
            quizQuestionalllist = quizQuestionService.quizQuestionList(pageable);
        } else {
            quizQuestionalllist = quizQuestionService.quizQuestionSearchList1(searchKeyword1, pageable);
        }

        //+1 <-- pageable 은 0부터 시작 +1 추가 1페이지부터시작
        int firstPage = quizQuestionalllist.getPageable().getPageNumber() + 1;
        //현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수
        //-1 <-- 값이 들어가는것을 막기위해 max 값 으로 두개 값을 넣고 더 큰 값을 넣어줌
        //.max =  현제 페이지에서 -4해줬을때 1보다 작은수가나오면 안되서 1혹은 현제 페이지 -4했을때 보다 큰값을 쓰기 위함
        int startPage = Math.max(firstPage - 9, 1);
        //현재 페이지에서 가장 뒤에 페이지 번호를 보여줄 변수
        //.min = 해당 번호가 넘어가버리면 안되기때문에 최소값을 구하는 방식
        int lastPage = Math.min(firstPage + 10, quizQuestionalllist.getTotalPages());
        int totalPages = quizQuestionalllist.getTotalPages(); // totalPages 정의


        model.addAttribute("quizQuestionalllist", quizQuestionalllist);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("totalPages", totalPages);

//        model.addAttribute("quizQuestions", quizQuestionService.findAll());
        return "quiz/listQuizQuestions"; // listQuizQuestions.html을 반환
    }


    // 사용자가 문제를 푸는 페이지 불러오기
    @GetMapping("/quiz/bQuiz")
    public String bQuiz(Model model, Principal principal, @AuthenticationPrincipal User username) {

        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());

            // 로그인한 사용자의 총 점수를 가져오는 로직 추가
            Long totalScore = scoreService.calculateTotalScore(username); // ScoreService 사용
            model.addAttribute("totalScore", totalScore); // 모델에 totalScore 추가
                   }



        List<QuizQuestion> allQuestions = quizQuestionService.findAll();
        Collections.shuffle(allQuestions); // 랜덤으로 문제 섞기
        List<QuizQuestion> randomQuestions = allQuestions.stream().limit(4).collect(Collectors.toList());
        model.addAttribute("quizQuestions", randomQuestions);
        return "quiz/bQuiz"; // bQuiz.html을 반환
    }

    // 사용자가 점수보는 페이지 호출
    @PostMapping("/quiz/submitAnswers")
    public String submitAnswers(@RequestParam Map<String, String> answers, Model model, Principal principal, @AuthenticationPrincipal User username) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken))
                ? (User) authentication.getPrincipal()
                : null;

        List<QuizQuestion> quizQuestions = quizQuestionService.findAll(); // 문제 가져오기

        //로그인시 닉네임 가져오기
        if (principal != null) {
            model.addAttribute("nickname", username.getNickname());
        }




            // 사용자가 답변한 결과를 처리
        for (QuizQuestion quiz : quizQuestions) {
            String userAnswer = answers.get("userAnswer" + quiz.getQuizQuestionId());

            // 사용자가 답변을 선택한 경우에만 처리
            if (userAnswer != null) {
                quiz.setUserAnswer(userAnswer);
                quiz.setIsCorrect(quiz.getCorrectAnswer().equals(userAnswer));

                // 정답 여부에 따라 scoreCorrect 설정
                Long scoreCorrect = quiz.isCorrect() ? 10L : 0L;

                // Score 객체 생성 및 설정
                Score score = new Score();
                score.setUser(user); // 사용자 설정
                score.setQuizQuestion(quiz); // 퀴즈 질문 설정
                score.setScoreQuestion(quiz.getQuestion()); // 질문 설정
                score.setScoreOptions(quiz.getOptions()); // 선택지 설정
                score.setScoreAnswer(userAnswer); // 사용자의 답변 설정
                score.setScoreCorrect(scoreCorrect); // 점수 설정

                // 점수 저장
                try {
                    scoreService.saveScore(score); // ScoreService를 통해 저장
                } catch (DataIntegrityViolationException e) {
                    model.addAttribute("errorMessage", "점수 저장 중 오류가 발생했습니다. 사용자 정보를 확인하세요.");
                }
            }
        }

        // 해당 페이지의 점수 계산
        int pageScore = 0;
        for (QuizQuestion quiz : quizQuestions) {
            if (quiz.isCorrect()) {
                pageScore += 10; // 문제당 10점
            }
        }
        model.addAttribute("pageScore", pageScore);

        // 총 점수 계산 (로그인한 경우에만)
        if (user != null) {
            Long totalScore = scoreService.calculateTotalScore(user); // 사용자 정보를 이용해 총 점수 계산
            model.addAttribute("totalScore", totalScore);
        } else {
            model.addAttribute("totalScore", null); // 로그인하지 않은 경우 총점은 null
        }

        model.addAttribute("quizQuestions", quizQuestions);
        return "quiz/scoreView"; // 결과 페이지로 이동
    }








}


