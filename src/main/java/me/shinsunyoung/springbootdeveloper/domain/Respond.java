package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@Table(name = "respond_tb")
public class Respond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "respond_id")
    private int respondId;

    @Column(name = "respond_content", length = 1000)
    private String respondContent;

    @CreationTimestamp
    @Column(name = "respond_date")
    private Timestamp respondDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User user;

    @Transient
    private String userNickname;

    public String getUserNickname() {
        return (user != null && user.getNickname() != null) ? user.getNickname() : "익명";
    }
}

