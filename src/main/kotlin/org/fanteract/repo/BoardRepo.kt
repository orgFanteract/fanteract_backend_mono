package org.fanteract.repo

import org.fanteract.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepo: JpaRepository<Board, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<Board>
}