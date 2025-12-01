package org.fanteract.dto

import java.time.LocalDateTime

data class CreateChatroomResponse(
    val chatroomId: Long,
)

data class ReadChatroomListResponse(
    val response: List<ReadChatroomResponse>
)

data class ReadChatroomResponse(
    val chatroomId: Long,
    val title: String,
    val description: String?,
)

data class JoinChatroomResponseDto(
    val userChatroomId: Long,
)

data class LeaveChatroomResponseDto(
    val userChatroomId: Long,
)

data class SendChatResponseDto(
    val userId: Long,
    val content: String,
    val createdAt: LocalDateTime,
)