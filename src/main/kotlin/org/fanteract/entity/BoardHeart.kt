package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity

@Entity
@Table(name = "board_hearts")
class BoardHeart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val boardHeartId: Long,
    val userId: Long,
    val boardId: Long,
): BaseEntity()