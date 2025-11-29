package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity


@Entity
@Table(name = "payments")
class Payment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paymentId: Long,
    val userId: Long,
    val second: Int,
    val cost: Int,
): BaseEntity()