package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.AnonymousUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface AnonymousUserMappingRepository extends JpaRepository<AnonymousUserMapping, Long> {
    @Nullable
    AnonymousUserMapping findByBoardIdAndUserId(Long boardId, Long userId);

    @Query("SELECT MAX(a.anonymousNumber) FROM AnonymousUserMapping a WHERE a.boardId = :boardId")
    Integer findMaxAnonymousNumberByBoardId(@Param("boardId") Long boardId);

    void deleteByBoardId(Long boardId);

    List<AnonymousUserMapping> findAllByBoardId(Long boardId);
}
