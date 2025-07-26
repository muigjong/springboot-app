package me.shinsunyoung.springbootdeveloper.service;

import me.shinsunyoung.springbootdeveloper.domain.Board;
import me.shinsunyoung.springbootdeveloper.domain.Respond;
import me.shinsunyoung.springbootdeveloper.exception.ResourceNotFoundException;
import me.shinsunyoung.springbootdeveloper.repository.RespondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespondService {

    @Autowired
    private RespondRepository respondRepository;

    public void saveRespond(Respond respond) {
        respondRepository.save(respond);
    }

    public List<Respond> getRespondsByBoard(Board board) {
        return respondRepository.findByBoard(board);
    }




    public Respond findById(int id) {
        return respondRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("답글을 찾을 수 없습니다."));
    }

    public void updateRespond(int id, String content) {
        Respond respond = findById(id);
        respond.setRespondContent(content);
        respondRepository.save(respond);
    }

    public void deleteRespond(int id) {
        respondRepository.deleteById(id);
    }




}

