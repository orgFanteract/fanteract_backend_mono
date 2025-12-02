package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity


@Entity
@Table(name = "payment_histories")
class PaymentHistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paymentHistoryId: Long = 0L,
    val userId: Long,
    val productId: Long,
): BaseEntity()