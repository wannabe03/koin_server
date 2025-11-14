package com.K_oin.Koin.Entitiy.BoardEntity;

import com.K_oin.Koin.Entitiy.UserEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "Koin_BoardScrapesTable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_koin_boardScraps_board_user",
                        columnNames = {"board_id", "user_id"}
                )
        },
        indexes = {
                @Index(name = "idx_board_scraps_board_id", columnList = "board_id"),
                @Index(name = "idx_board_scraps_user_id", columnList = "user_id"),
        }
) // 중복 스크랩 방지를 위한 유니크 제약조건
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardScraps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
