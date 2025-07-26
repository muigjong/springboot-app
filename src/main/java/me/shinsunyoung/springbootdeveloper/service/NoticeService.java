package me.shinsunyoung.springbootdeveloper.service;

import me.shinsunyoung.springbootdeveloper.domain.Notice;
import me.shinsunyoung.springbootdeveloper.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    //공지사항 저장
    public void saveNotice(String title, String imagePath, String text) {
        Notice notice = new Notice();
        notice.setNoticeTitle(title);
        notice.setNoticeImage(imagePath); // 이미지 경로
        notice.setNoticeText(text);
        noticeRepository.save(notice);
    }

    //공지사항 목록 불러오기
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll(); // 모든 공지사항을 가져옵니다.
    }



    // 공지사항 ID로 조회
    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    // 공지사항 수정
    public void updateNotice(Long id, String title, String imagePath, String text) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        notice.setNoticeTitle(title);
        if (imagePath != null) {
            notice.setNoticeImage(imagePath);
        }
        notice.setNoticeText(text);
        noticeRepository.save(notice); // 수정된 공지사항 저장
    }

    // 공지사항 삭제
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }



}