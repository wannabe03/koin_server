package com.K_oin.Koin.Entitiy.BoardEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "Koin_AnonymousUserMapping",
        indexes = {
                @Index(name = "idx_board_user", columnList = "board_id, user_id", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousUserMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long boardId; // 게시글 기준

    @Column(nullable = false)
    private Long userId; // 실제 유저 (외래키 아님, 단순히 기록용)

    @Column(nullable = false)
    private Integer anonymousNumber;
}
