package org.fanteract.domain

import org.fanteract.repo.ChatRepo
import org.springframework.stereotype.Component

@Component
class ChatReader(
    private val chatRepo: ChatRepo,
) {
}