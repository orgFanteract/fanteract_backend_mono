package org.fanteract.repo

import org.fanteract.entity.BoardHeart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardHeartRepo: JpaRepository<BoardHeart, Long> {
    fun findByBoardIdIn(idList: List<Long>): List<BoardHeart>
}