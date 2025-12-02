package org.fanteract.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.fanteract.domain.BoardReader
import org.fanteract.domain.ChatReader
import org.fanteract.domain.ChatroomReader
import org.fanteract.domain.CommentReader
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.ActivityStats
import org.fanteract.dto.ReadMyPageResponse
import org.fanteract.dto.ReadRestrictedBoardListResponse
import org.fanteract.dto.ReadRestrictedBoardResponse
import org.fanteract.dto.ReadRestrictedChatListResponse
import org.fanteract.dto.ReadRestrictedChatResponse
import org.fanteract.dto.ReadRestrictedCommentListResponse
import org.fanteract.dto.ReadRestrictedCommentResponse
import org.fanteract.dto.RestrictionStats
import org.fanteract.dto.UserScore
import org.fanteract.dto.UserSignInRequestDto
import org.fanteract.dto.UserSignUpRequestDto
import org.fanteract.dto.UserSignInResponseDto
import org.fanteract.enumerate.RiskLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val chatroomReader: ChatroomReader,
    private val chatReader: ChatReader,
    private val boardReader: BoardReader,
    private val commentReader: CommentReader,
    @Value($$"${jwt.secret}") private val jwtSecret: String,
) {
    fun signIn(userSignInRequestDto: UserSignInRequestDto): UserSignInResponseDto {
        val user = userReader.findByEmail(userSignInRequestDto.email)

        if (user.password != userSignInRequestDto.password){
            throw NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")
        }

        val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        val token = Jwts.builder().subject(user.userId.toString()).signWith(secretKey).compact()

        return UserSignInResponseDto(token)
    }

    fun signUp(userSignUpRequestDto: UserSignUpRequestDto) {
        userWriter.create(
            email = userSignUpRequestDto.email,
            password = userSignUpRequestDto.password,
            name = userSignUpRequestDto.name,
        )
    }

    fun readMyPage(userId: Long): ReadMyPageResponse {
        val user = userReader.findById(userId)

        val chatroomCount = chatroomReader.countByUserId(user.userId)
        val chatCount = chatReader.countByUserId(user.userId)
        val boardCount = boardReader.countByUserId(user.userId)
        val commentCount = commentReader.countByUserId(user.userId)

        val restrictedChatCount = chatReader.countByUserIdAndRiskLevel(user.userId, RiskLevel.BLOCK)
        val restrictedBoardCount = boardReader.countByUserIdAndRiskLevel(user.userId, RiskLevel.BLOCK)
        val restrictedCommentCount = commentReader.countByUserIdAndRiskLevel(user.userId, RiskLevel.BLOCK)

        val activityStats =
            ActivityStats(
                totalChatRoomCount = chatroomCount,
                totalChatCount = chatCount,
                totalBoardCount = boardCount,
                totalCommentCount = commentCount,
            )
        val restrictionStats =
            RestrictionStats(
                totalRestrictedChatCount = restrictedChatCount,
                totalRestrictedBoardCount = restrictedBoardCount,
                totalRestrictedCommentCount = restrictedCommentCount,
            )
        val userScore = 
            UserScore(
                activePoint = user.activePoint,
                abusePoint = user.abusePoint,
                balance = user.balance,
        )

        return ReadMyPageResponse(
            email = user.email,
            name = user.name,
            activityStats = activityStats,
            restrictionStats = restrictionStats,
            userScore = userScore
        )
    }

    fun readRestrictedBoard(userId: Long, page: Int, size: Int): ReadRestrictedBoardListResponse {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))

        val boardPage = boardReader.findByUserIdAndRiskLevel(userId, RiskLevel.BLOCK, pageable)

        val contents = boardPage.content.map { board ->
            ReadRestrictedBoardResponse(
                boardId = board.boardId,
                title = board.title,
                content = board.content,
                riskLevel = board.riskLevel
            )
        }

        return ReadRestrictedBoardListResponse(
            contents = contents,
            page = page,
            size = size,
            totalElements = boardPage.totalElements,
            totalPages = boardPage.totalPages,
            hasNext = boardPage.hasNext()
        )
    }
    fun readRestrictedComment(userId: Long, page: Int, size: Int): ReadRestrictedCommentListResponse {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))

        val commentPage = commentReader.findByUserIdAndRiskLevel(userId, RiskLevel.BLOCK, pageable)

        val contents = commentPage.content.map { comment ->
            ReadRestrictedCommentResponse(
                commentId = comment.commentId,
                content = comment.content,
                riskLevel = comment.riskLevel
            )
        }

        return ReadRestrictedCommentListResponse(
            contents = contents,
            page = page,
            size = size,
            totalElements = commentPage.totalElements,
            totalPages = commentPage.totalPages,
            hasNext = commentPage.hasNext()
        )
    }
    fun readRestrictedChat(userId: Long, page: Int, size: Int): ReadRestrictedChatListResponse {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val chatPage = chatReader.findByUserIdAndRiskLevel(userId, RiskLevel.BLOCK, pageable)

        val contents = chatPage.content.map { chat ->
            ReadRestrictedChatResponse(
                chatId = chat.chatId,
                content = chat.content,
                riskLevel = chat.riskLevel
            )
        }

        return ReadRestrictedChatListResponse(
            contents = contents,
            page = page,
            size = size,
            totalElements = chatPage.totalElements,
            totalPages = chatPage.totalPages,
            hasNext = chatPage.hasNext()
        )
    }

}