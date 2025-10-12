package com.K_oin.Koin.Entitiy.UserEntity;

import com.K_oin.Koin.EnumData.Nationality;
import com.K_oin.Koin.EnumData.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Koin_UserTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 고객 로그인 Id

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String name; // 고객 이름

    private LocalDate birthDate;

    @Column(length = 50, unique = true)
    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Nationality nationality;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role; //

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
