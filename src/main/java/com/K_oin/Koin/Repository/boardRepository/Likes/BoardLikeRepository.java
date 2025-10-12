package com.K_oin.Koin.Repository.boardRepository.Likes;

import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.Entitiy.BoardEntity.Likes.BoardLike;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    BoardLike findByUserAndBoard(User user, Board board);
}
