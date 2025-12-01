package org.fanteract.dto

import java.time.LocalDateTime

data class ReadCommentListResponse(
    val contents: List<ReadCommentResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadCommentResponse(
    val commentId: Long,
    val content: String,
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class CreateCommentResponse(
    val commentId: Long,
)