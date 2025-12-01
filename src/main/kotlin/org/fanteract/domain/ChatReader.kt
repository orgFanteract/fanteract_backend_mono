package org.fanteract.domain

import org.fanteract.entity.Chat
import org.fanteract.repo.ChatRepo
import org.springframework.stereotype.Component
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@Component
class ChatReader(
    private val chatRepo: ChatRepo,
) {
    fun findByUserIdAndChatroomId(
        userId: Long,
        chatroomId: Long,
        pageable: Pageable
    ): Page<Chat> {
        return chatRepo.findByUserIdAndChatroomId(userId, chatroomId, pageable)
    }

    fun findByUserIdAndChatroomIdAnd(
        userId: Long,
        chatroomId: Long,
        content: String,
        pageable: PageRequest
    ): Page<Chat> {
        return chatRepo.findByUserIdAndChatroomIdAndContentContaining(userId, chatroomId, content, pageable)
    }
}