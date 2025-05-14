package com.example.assignment.auth.entity;

import com.example.assignment.common.enums.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    //생성자
    private User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = UserRole.USER;
    }

    //생성 메서드
    public static User of(String username, String password, String nickname) {
        return new User(username, password, nickname);
    }

    //어드민 권한 부여
    public void setAdmin(){
        this.userRole = UserRole.ADMIN;
    }
}
