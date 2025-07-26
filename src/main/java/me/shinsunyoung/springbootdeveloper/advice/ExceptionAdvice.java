package me.shinsunyoung.springbootdeveloper.advice;


import lombok.extern.slf4j.Slf4j;
import me.shinsunyoung.springbootdeveloper.exception.*;
import me.shinsunyoung.springbootdeveloper.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {


    //401 응답
    @ExceptionHandler(UserNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response userNotEqualsException(){
        return Response.failure(401, "유저 정보 불일치");
    }

    //404 응답
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response userNotFoundException (){
        return Response.failure(404,"요청 회원을 찾을수 없음");
    }

    







}
