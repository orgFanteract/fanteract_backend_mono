package org.fanteract.domain

import org.fanteract.entity.Chatroom
import org.fanteract.repo.ChatroomRepo
import org.springframework.stereotype.Component

@Component
class ChatroomWriter(
    private val chatroomRepo: ChatroomRepo,
) {
    fun create(
        title: String,
        description: String?,
        userId: Long
    ): Chatroom {
        return chatroomRepo.save(
            Chatroom(
                title = title,
                description = description,
                userId = userId,
            )
        )

    }
}