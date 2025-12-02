package org.fanteract.dto

import org.fanteract.enumerate.RiskLevel
import java.time.LocalDateTime

data class CreateBoardResponse(
    val boardId: Long?,
    val riskLevel: RiskLevel,
)

data class ReadBoardListResponse(
    val contents: List<ReadBoardResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadBoardResponse(
    val boardId: Long,
    val userName: String,
    val title: String,
    val commentCount: Int,
    val heartCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class ReadBoardDetailResponse(
    val boardId: Long,
    val userName: String,
    val title: String,
    val content: String,
    val commentCount: Int,
    val heartCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class CreateHeartInBoardResponse(
    val boardHeartId: Long,
)
