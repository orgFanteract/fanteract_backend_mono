package org.fanteract.domain

import org.fanteract.entity.Board
import org.fanteract.entity.Chat
import org.fanteract.repo.BoardRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class BoardReader(
    private val boardRepo: BoardRepo,
) {
    fun readByUserId(pageable: Pageable, userId: Long): Page<Board> {
        return boardRepo.findByUserId(userId, pageable)
    }

    fun findAll(
        pageable: Pageable
    ): Page<Board> {
        return boardRepo.findAll(pageable)
    }

    fun readById(boardId: Long): Board {
        return boardRepo.findById(boardId).orElseThrow{NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")}
    }

    fun existsById(boardId: Long): Boolean {
        return boardRepo.existsById(boardId)
    }

    fun countByUserId(userId: Long): Long {
        return boardRepo.countByUserId(userId)
    }
}