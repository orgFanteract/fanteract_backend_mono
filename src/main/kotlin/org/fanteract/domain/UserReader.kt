package org.fanteract.domain

import org.fanteract.entity.User
import org.fanteract.repo.UserRepo
import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepo: UserRepo,
) {
    fun findByEmail(email: String): User {
        return userRepo.findByEmail(email) ?: throw NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")
    }

    fun findByIdIn(idList: List<Long>): List<User> {
        return userRepo.findByUserIdIn(idList)
    }

    fun findById(userId: Long): User {
        return userRepo.findById(userId).orElseThrow{NoSuchElementException("조건에 맞는 사용자가 존재하지 않습니다")}
    }
}