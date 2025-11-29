package org.fanteract.repo

import org.fanteract.entity.UserChatroom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserChatroomRepo: JpaRepository<UserChatroom, Long> {}