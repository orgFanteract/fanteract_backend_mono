package org.fanteract.domain

import org.fanteract.entity.Chat
import org.fanteract.entity.Chatroom
import org.fanteract.repo.ChatRepo
import org.springframework.stereotype.Component
import kotlin.String

@Component
class ChatWriter(
    private val chatRepo: ChatRepo,
) {
    fun create(
        content: String,
        chatroomId: Long,
        userId: Long
    ): Chat {
        return chatRepo.save(
            Chat(
                content = content,
                chatroomId = chatroomId,
                userId = userId,
            )
        )
    }

}