package org.fanteract.service

import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.UserSignInRequestDto
import org.fanteract.dto.UserSignUpRequestDto
import org.fanteract.repo.UserRepo
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
) {
    fun signIn(userSignInRequestDto: UserSignInRequestDto) {
        val user = userReader.findByEmail(userSignInRequestDto.email)

        if (user.password != userSignInRequestDto.password){
            throw NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")
        }
    }

    fun signUp(userSignUpRequestDto: UserSignUpRequestDto) {
        userWriter.create(
            email = userSignUpRequestDto.email,
            password = userSignUpRequestDto.password,
        )
    }

}