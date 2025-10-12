package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import com.K_oin.Koin.EnumData.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = {"author", "likes", "comments"})
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"author", "likes", "comments"})
    Page<Board> findAllByBoardTypeOrderByCreatedAtDesc(BoardType boardType, Pageable pageable);

    @Query("""
    SELECT b
    FROM Board b
    WHERE b.boardType = :boardType
    ORDER BY (SELECT COUNT(l) FROM BoardLike l WHERE l.board = b) DESC,
             b.createdAt DESC
    """)
    @EntityGraph(attributePaths = {"author", "likes", "comments"})
    Page<Board> findAllByBoardTypeOrderByLikesDesc(BoardType boardType, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "likes", "comments", "comments.author"})
    Optional<Board> findByBoardTypeAndBoardId(BoardType boardType, Long boardId);
}
