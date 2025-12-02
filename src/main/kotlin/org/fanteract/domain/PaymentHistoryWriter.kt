package org.fanteract.domain

import org.fanteract.entity.PaymentHistory
import org.fanteract.repo.PaymentHistoryRepo
import org.springframework.stereotype.Component

@Component
class PaymentHistoryWriter(
    private val paymentHistoryRepo: PaymentHistoryRepo
) {
    fun create(userId: Long, productId: Long): PaymentHistory {
        return paymentHistoryRepo.save(
            PaymentHistory(
                userId = userId,
                productId = productId,
            )
        )
    }

}