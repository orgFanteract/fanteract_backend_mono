package org.fanteract.repo

import org.fanteract.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentRepo: JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.status = 'ACTIVATED'")
    fun findByBoardId(
        @Param("boardId") boardId: Long,
        pageable: Pageable
    ): Page<Comment>

    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.status = 'ACTIVATED'")
    fun findByBoardId(
        @Param("boardId") boardId: Long,
    ): List<Comment>

    @Query("SELECT c FROM Comment c WHERE c.userId = :userId AND c.status = 'ACTIVATED'")
    fun findByUserId(
        @Param("userId") userId: Long,
        pageable: Pageable
    ): Page<Comment>

    @Query("SELECT c FROM Comment c WHERE c.boardId IN :idList AND c.status = 'ACTIVATED'")
    fun findByBoardIdIn(
        @Param("idList") idList: List<Long>
    ): List<Comment>
}