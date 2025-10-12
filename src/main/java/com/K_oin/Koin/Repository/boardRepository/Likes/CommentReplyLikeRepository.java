package com.K_oin.Koin.Repository.boardRepository.Likes;

import com.K_oin.Koin.Entitiy.BoardEntity.CommentReply;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.CommentReplyLike;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyLikeRepository extends JpaRepository<CommentReplyLike, Long> {
    CommentReplyLike findByUserAndCommentReply(User user, CommentReply commentReply);
}
