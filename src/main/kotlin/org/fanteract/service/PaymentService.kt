package org.fanteract.service

import org.fanteract.domain.PaymentHistoryWriter
import org.fanteract.domain.ProductReader
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.PurchaseProductResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PaymentService(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val paymentHistoryWriter: PaymentHistoryWriter,
    private val productReader: ProductReader,
) {
    fun purchaseProduct(productId: Long, userId: Long): PurchaseProductResponse {
        val product = productReader.findById(productId)

        // user balance 갱신
        val user = userReader.findById(userId)

        userWriter.updateBalance(
            userId = user.userId,
            balance = product.cost
        )

        // payment 기록
        val response =
            paymentHistoryWriter.create(
                userId = userId,
                productId = productId,
            )

        return PurchaseProductResponse(paymentHistoryId = response.paymentHistoryId)
    }
}