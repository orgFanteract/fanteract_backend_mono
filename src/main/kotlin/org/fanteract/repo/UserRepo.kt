package org.fanteract.repo

import org.fanteract.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUserIdIn(idList: List<Long>): List<User>
}