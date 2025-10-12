package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.BoardComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<BoardComment, Long> {

    @EntityGraph(attributePaths = {"replies.author", "replies.likes"})
    Optional<BoardComment> findWithRepliesAndLikesByCommentId(Long commentId);
}
