package org.fanteract.repo

import org.fanteract.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepo: JpaRepository<Payment, Long>