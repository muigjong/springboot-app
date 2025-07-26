package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Getter;
import me.shinsunyoung.springbootdeveloper.domain.Role;

import java.security.Timestamp;

@Getter
public class UserDto {
    private Long userId;
    private String password;
    private String name;
    private String nickname;
    private String birthday;
    private String gender;
    private String email;
    private String phone;
    private String add1;
    private String add2;
    private String zipcode;
    private Role role;
    private Timestamp userDate;


}
