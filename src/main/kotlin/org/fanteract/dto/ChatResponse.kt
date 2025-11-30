package org.fanteract.dto

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