package com.K_oin.Koin.Entitiy.BoardEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "Koin_BoardImageTable",
        indexes = {
                @Index(name = "idx_boardImage_board_id", columnList = "board_id"),
                @Index(name = "idx_boardImage_sort_order", columnList = "sortOrder")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "imageId")
@ToString(exclude = {"board"})
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String imageUrl;  // S3 URL 저장

    private int sortOrder; // 이미지 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}