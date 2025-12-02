package org.fanteract.repo

import org.fanteract.entity.PaymentHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentHistoryRepo: JpaRepository<PaymentHistory, Long>