package org.fanteract.domain

import org.fanteract.entity.User
import org.fanteract.repo.UserRepo
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.String

@Component
class UserWriter(
    private val userRepo: UserRepo,
) {
    fun create(
        email: String,
        password: String,
        name: String,
    ): User {
        return userRepo.save(
            User(
                email = email,
                name = name,
                password = password,
            )
        )
    }

    fun updateActivePoint(
        userId: Long,
        activePoint: Int
    ) {
        val user = userRepo.findById(userId).orElseThrow{NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")}
        user.activePoint += activePoint

        userRepo.save(user)
    }

    fun updateAbusePoint(
        userId: Long,
        abusePoint: Int
    ) {
        val user = userRepo.findById(userId).orElseThrow{NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")}
        user.abusePoint += abusePoint

        userRepo.save(user)
    }
}