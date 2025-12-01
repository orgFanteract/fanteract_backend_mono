package org.fanteract.domain

import org.fanteract.repo.UserChatroomHistoryRepo
import org.springframework.stereotype.Component

@Component
class UserChatroomHistoryReader(
    private val userChatroomHistoryRepo: UserChatroomHistoryRepo,
) {

}