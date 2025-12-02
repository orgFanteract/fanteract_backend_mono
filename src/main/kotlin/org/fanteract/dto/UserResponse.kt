package org.fanteract.dto

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