package org.fanteract.repo

import org.fanteract.entity.UserChatroom
import org.fanteract.entity.UserChatroomHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserChatroomHistoryRepo: JpaRepository<UserChatroomHistory, Long> {
}