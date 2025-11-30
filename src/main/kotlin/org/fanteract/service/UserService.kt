package org.fanteract.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.UserSignInRequestDto
import org.fanteract.dto.UserSignUpRequestDto
import org.fanteract.dto.UserSignInResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
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
            )
    }

}