package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.Entitiy.BoardEntity.BoardScraps;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardScrapRepository extends JpaRepository<BoardScraps, Long> {
    BoardScraps findByUserAndBoard(User user, Board board);

    long countByBoard(Board board);

    void deleteByBoard_BoardId(Long boardId);
}
