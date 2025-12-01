package org.fanteract.dto

data class CreateChatroomRequest(
    val title: String,
    val description: String?,
)

data class SendChatRequestDto(
    val content: String,
)

data class ReadChatContainingRequest(
    val content: String,
)