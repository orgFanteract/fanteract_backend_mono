package org.fanteract.domain

import org.fanteract.entity.Chatroom
import org.fanteract.repo.ChatroomRepo
import org.springframework.stereotype.Component

@Component
class ChatroomReader(
    private val chatroomRepo: ChatroomRepo,
) {
    fun findByUserId(userId: Long): List<Chatroom> {
        return chatroomRepo.findByUserId(userId)
    }
    fun findByChatroomIdAndUserId(
        chatroomId: Long,
        userId: Long
    ): Chatroom {
        val chatroom =
            chatroomRepo.findById(chatroomId)
                .orElseThrow{NoSuchElementException("조건에 맞는 채팅방이 존재하지 않습니다")}

        if (chatroom.userId != userId){
            throw NoSuchElementException("조건에 맞는 채팅방이 존재하지 않습니다")
        }

        return chatroom
    }

    fun existsById(chatroomId: Long){
        if (!chatroomRepo.existsById(chatroomId)){
            throw NoSuchElementException("조건에 맞는 채팅방이 존재하지 않습니다")
        }
    }

    fun findByUserIdAndTitleContaining(userId: Long, title: String): List<Chatroom> {
        return chatroomRepo.findByUserIdAndTitleContaining(userId, title)
    }

    fun countByUserId(userId: Long): Long {
        return chatroomRepo.countByUserId(userId)
    }

}
