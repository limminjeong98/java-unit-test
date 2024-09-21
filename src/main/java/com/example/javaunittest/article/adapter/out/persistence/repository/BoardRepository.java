package com.example.javaunittest.article.adapter.out.persistence.repository;

import com.example.javaunittest.article.adapter.out.persistence.entity.BoardJpaEntity;
import com.example.javaunittest.article.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardJpaEntity, Long> {
    List<BoardJpaEntity> findAllByBoardType(BoardType boardType);
}