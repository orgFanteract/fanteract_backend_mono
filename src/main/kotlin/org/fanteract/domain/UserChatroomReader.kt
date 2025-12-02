package org.fanteract.domain

import org.fanteract.entity.UserChatroom
import org.fanteract.repo.UserChatroomRepo
import org.springframework.stereotype.Component

@Component
class UserChatroomReader(
    private val userChatroomRepo: UserChatroomRepo,
) {
    fun findByUserIdAndChatroomId(userId: Long, chatroomId: Long): UserChatroom? {
        return userChatroomRepo.findByUserIdAndChatroomId(
            userId = userId,
            chatroomId = chatroomId,
        ).firstOrNull()
    }
}