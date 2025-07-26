package me.shinsunyoung.springbootdeveloper.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {

    private int boardId;
    private String boardTitle;
    private String boardContent;
    private Timestamp boardDate;

    private String writer;  //작성자
    private Boolean deleteYn;     //삭제
    private LocalDateTime modifiedDate;   //최종수정일
    private boolean boardSecret;   //비밀글

    @Builder
    public BoardDto(int boardId, String boardTitle, String boardContent,
                    Timestamp boardDate,
                    String writer, boolean deleteYn, LocalDateTime modifiedDate, boolean boardSecret) {

        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardDate = boardDate;
        this.writer = writer;
        this.deleteYn = deleteYn;
        this.modifiedDate = modifiedDate;
        this.boardSecret = boardSecret;
    }



}
