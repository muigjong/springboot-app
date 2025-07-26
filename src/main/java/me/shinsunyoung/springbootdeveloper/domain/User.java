package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


//회원 관련


@Getter
@Setter
@Data
@Entity
@Table(name = "user_tb")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String password;


    private String name;

    private String nickname;

    private String birthday;


   private String email;


    private String phone;

    private String church;


    @Enumerated(EnumType.STRING)

    private Role role;

    //Timestamp는 java.sql
    @CreationTimestamp
    private Timestamp userDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Score> scores; // 사용자가 가진 점수 목록

    // 기본 생성자
    public User() {
    }


    @Builder
    public User(Long userId,  String password, String name, String nickname, String birthday, String church, String email,
                String phone, Role role, Timestamp userDate) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthday = birthday;
        this.church = church;
        this.email = email;
        this.phone = phone;
        this.role = Role.USER;
        this.userDate = userDate;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    //비밀번호 재설정
    public void setPassword(String password) {
        this.password = password;
    }
}
