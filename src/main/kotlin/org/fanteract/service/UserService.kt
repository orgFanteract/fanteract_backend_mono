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
import org.fanteract.dto.RestrictionStats
import org.fanteract.dto.UserScore
import org.fanteract.dto.UserSignInRequestDto
import org.fanteract.dto.UserSignUpRequestDto
import org.fanteract.dto.UserSignInResponseDto
import org.springframework.beans.factory.annotation.Value
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
        val user =
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

        val activityStats =
            ActivityStats(
                totalChatRoomCount = chatroomCount,
                totalChatCount = chatCount,
                totalBoardCount = boardCount,
                totalCommentCount = commentCount,
            )
        val restrictionStats =
            RestrictionStats(
                totalRestrictedChatCount = 0,
                totalRestrictedBoardCount = 0,
                totalRestrictedCommentCount = 0,
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

}