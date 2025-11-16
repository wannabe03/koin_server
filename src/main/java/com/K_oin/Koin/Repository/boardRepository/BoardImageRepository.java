package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.Board;
import com.K_oin.Koin.Entitiy.BoardEntity.BoardImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
