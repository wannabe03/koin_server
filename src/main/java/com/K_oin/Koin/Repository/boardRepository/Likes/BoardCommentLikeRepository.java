package com.K_oin.Koin.Repository.boardRepository.Likes;

import com.K_oin.Koin.Entitiy.BoardEntity.BoardComment;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.BoardCommentLike;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentLikeRepository extends JpaRepository<BoardCommentLike, Long> {
    BoardCommentLike findByUserAndBoardComment(User user, BoardComment boardComment);
}
