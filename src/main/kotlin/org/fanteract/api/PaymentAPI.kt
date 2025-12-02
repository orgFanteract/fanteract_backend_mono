package org.fanteract.api

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
import org.fanteract.dto.PurchaseProductResponse
import org.fanteract.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
class PaymentAPI(
    private val paymentService: PaymentService,
) {
    @LoginRequired
    @Operation(summary = "상품 구매")
    @PostMapping("{productId}/product")
    fun purchaseProduct(
        request: HttpServletRequest,
        @PathVariable productId: Long,
    ): ResponseEntity<PurchaseProductResponse> {
        val userId = JwtParser.extractKey(request, "userId")
        val response = paymentService.purchaseProduct(productId, userId)

        return ResponseEntity.ok().body(response)
    }
}