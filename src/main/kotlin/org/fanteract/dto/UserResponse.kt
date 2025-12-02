package org.fanteract.dto

import org.fanteract.enumerate.RiskLevel

data class UserSignInResponseDto(
    val token: String,
)

data class ReadMyPageResponse(
    val email: String,
    val name: String,

    val activityStats: ActivityStats,
    val restrictionStats: RestrictionStats,
    val userScore: UserScore,
)

data class ActivityStats(
    val totalChatRoomCount: Long,
    val totalChatCount: Long,
    val totalBoardCount: Long,
    val totalCommentCount: Long,
)

data class RestrictionStats(
    val totalRestrictedChatCount: Long,
    val totalRestrictedBoardCount: Long,
    val totalRestrictedCommentCount: Long,
)

data class UserScore(
    val activePoint: Int,
    val abusePoint: Int,
    val balance: Int,
)

data class ReadRestrictedBoardListResponse(
    val contents: List<ReadRestrictedBoardResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadRestrictedCommentListResponse(
    val contents: List<ReadRestrictedCommentResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadRestrictedChatListResponse(
    val contents: List<ReadRestrictedChatResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadRestrictedBoardResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val riskLevel: RiskLevel,
)

data class ReadRestrictedCommentResponse(
    val commentId: Long,
    val content: String,
    val riskLevel: RiskLevel,
)

data class ReadRestrictedChatResponse(
    val chatId: Long,
    val content: String,
    val riskLevel: RiskLevel,
)