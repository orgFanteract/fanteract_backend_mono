package org.fanteract.domain

import org.fanteract.entity.UserChatroomHistory
import org.fanteract.enumerate.ChatroomJoinStatus
import org.fanteract.repo.UserChatroomHistoryRepo
import org.springframework.stereotype.Component
import kotlin.Long

@Component
class UserChatroomHistoryWriter(
    private val userChatroomHistoryRepo: UserChatroomHistoryRepo,
) {
    fun create(
        userId: Long,
        chatroomId: Long,
        chatroomJoinStatus: ChatroomJoinStatus,
    ): UserChatroomHistory {
        val userChatroomHistory =
            userChatroomHistoryRepo.save(
                UserChatroomHistory(
                    userId = userId,
                    chatroomId = chatroomId,
                    chatroomJoinStatus = chatroomJoinStatus,
                )
            )

        return userChatroomHistory
    }
}