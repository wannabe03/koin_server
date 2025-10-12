package com.K_oin.Koin.Entitiy.BoardEntity.Likes;

import com.K_oin.Koin.Entitiy.BoardEntity.BoardComment;
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
        name = "Koin_BoardCommentLikeTable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_koin_board_comment_like_board_user",
                        columnNames = {"comment_id", "user_id"}
                )
        }
) // 중복 좋아요 방지를 위한 유니크 제약조건
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private BoardComment boardComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
