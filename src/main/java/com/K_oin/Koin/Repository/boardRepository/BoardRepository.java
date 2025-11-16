package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.EnumData.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @EntityGraph(attributePaths = {
            "author",
            "likes",
            "comments",
            "comments.author",
            "comments.likes",
            "comments.replies",
            "comments.replies.author",
            "comments.replies.likes",
            "images"
    })
    Optional<Board> findByBoardTypeAndBoardId(BoardType boardType, Long boardId);

    @Query(value = """
    SELECT DISTINCT b.*
    FROM mainkoinserver.koin_board_table b
    WHERE b.board_id IN (
        SELECT c.board_id
        FROM mainkoinserver.koin_board_comment_table c
        WHERE c.user_id = :userId
        UNION
        SELECT bc.board_id
        FROM mainkoinserver.koin_comment_reply_table r
        JOIN mainkoinserver.koin_board_comment_table bc ON r.parent_comment_id = bc.comment_id
        WHERE r.user_id = :userId
    )
    ORDER BY b.created_at DESC
    """, nativeQuery = true)
    List<Board> findDistinctBoardsByMyCommentUserId(@Param("userId") Long userId);

    @Query(value = """
    SELECT DISTINCT b.*
    FROM mainkoinserver.koin_board_table b
    WHERE b.board_id IN (
        SELECT bl.board_id
        FROM mainkoinserver.koin_board_like_table bl
        WHERE bl.user_id = :userId
    )
    ORDER BY b.created_at DESC
    """, nativeQuery = true)
    List<Board> findDistinctBoardsByMyLikeUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"images"})
    Optional<Board> findByBoardId(Long boardId);
}
