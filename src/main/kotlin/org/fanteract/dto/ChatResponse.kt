package org.fanteract.dto

import org.fanteract.enumerate.RiskLevel
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

data class ReadChatResponse(
    val chatId: Long,
    val userName: String,
    val content: String,
    val createdAt: LocalDateTime,
)

data class ReadChatListResponse(
    val contents: List<ReadChatResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadChatContainingResponse(
    val chatId: Long,
    val userName: String,
    val content: String,
    val createdAt: LocalDateTime,
)

data class ReadChatContainingListResponse(
    val contents: List<ReadChatContainingResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class JoinChatroomResponse(
    val userChatroomId: Long,
)

data class LeaveChatroomResponseDto(
    val userChatroomId: Long,
)

data class SendChatResponse(
    val chatId: Long? = null,
    val userName: String? = null,
    val content: String? = null,
    val createdAt: LocalDateTime? = null,
    val riskLevel: RiskLevel,
)