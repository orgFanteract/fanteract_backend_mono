package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "chatrooms")
class Chatroom (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val chatroomId: Long,
    val title: String,
    val description: String,
)