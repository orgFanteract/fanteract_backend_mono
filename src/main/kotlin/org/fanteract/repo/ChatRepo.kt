package org.fanteract.repo

import org.fanteract.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@Repository
interface ChatRepo: JpaRepository<Chat, Long>{
    fun findByUserIdAndChatroomId(
        userId: Long,
        chatroomId: Long,
        pageable: Pageable
    ): Page<Chat>

    fun findByUserIdAndChatroomIdAndContentContaining(
        userId: Long,
        chatroomId: Long,
        content: String,
        pageable: PageRequest
    ): Page<Chat>

    fun countByUserId(userId: Long): Long
}