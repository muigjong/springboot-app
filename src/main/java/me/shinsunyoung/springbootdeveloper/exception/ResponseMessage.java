package me.shinsunyoung.springbootdeveloper.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {

    private String message;
    private boolean success;

    public ResponseMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
