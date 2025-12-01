package org.fanteract.repo

import org.fanteract.entity.Chatroom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatroomRepo: JpaRepository<Chatroom, Long> {
    fun findByUserId(userId: Long): List<Chatroom>
    fun findByUserIdAndTitleContaining(userId: Long, title: String): List<Chatroom>
}