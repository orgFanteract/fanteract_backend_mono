package org.fanteract.dto

import org.fanteract.enumerate.RiskLevel
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
    val heartCount: Int,
    val userName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class CreateCommentResponse(
    val commentId: Long?,
    val riskLevel: RiskLevel,
)

data class CreateHeartInCommentResponse(
    val commentHeartId: Long,
)