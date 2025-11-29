package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity

@Entity
@Table(name = "chats")
class Chat (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val content: String,
    val chatroomId: Long,
    val userId: Long,
): BaseEntity()