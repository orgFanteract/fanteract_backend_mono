package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity
import org.fanteract.enumerate.ChatroomJoinStatus

@Entity
@Table(name = "user_chatrooms")
class UserChatroom (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userChatroomId: Long = 0L,
    var userId: Long,
    var chatroomId: Long,
    @Enumerated(EnumType.STRING)
    var chatroomJoinStatus: ChatroomJoinStatus,
): BaseEntity()