package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity

@Entity
@Table(name = "comment_hearts")
class CommentHeart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentHeartId: Long,
    val userId: Long,
    val commentId: Long,
): BaseEntity()