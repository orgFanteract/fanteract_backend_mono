package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity

@Entity
@Table(name = "products")
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val productId: Long,
    val name: String,
    val cost: Int,
): BaseEntity()