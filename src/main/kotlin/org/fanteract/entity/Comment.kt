package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity

@Entity
@Table(name = "comments")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId: Long = 0L,
    var content: String,
    val boardId: Long,
    val userId: Long,
): BaseEntity()