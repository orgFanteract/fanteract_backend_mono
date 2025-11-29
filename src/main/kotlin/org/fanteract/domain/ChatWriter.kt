package org.fanteract.domain

import org.fanteract.entity.Chatroom
import org.fanteract.repo.ChatRepo
import org.springframework.stereotype.Component

@Component
class ChatWriter(
    private val chatRepo: ChatRepo,
) {

}