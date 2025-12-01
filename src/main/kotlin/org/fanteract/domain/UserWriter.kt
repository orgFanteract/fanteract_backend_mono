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
}