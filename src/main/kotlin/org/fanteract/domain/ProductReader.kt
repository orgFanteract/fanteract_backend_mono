package org.fanteract.domain

import org.fanteract.entity.Product
import org.fanteract.repo.ProductRepo
import org.springframework.stereotype.Component

@Component
class ProductReader(
    private val productRepo: ProductRepo,
) {
    fun findById(productId: Long): Product {
        return productRepo.findById(productId).orElseThrow{NoSuchElementException("조건에 맞는 상품이 존재하지 않습니다")}
    }

}