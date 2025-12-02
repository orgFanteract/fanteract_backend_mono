package org.fanteract.repo

import org.fanteract.entity.Alarm
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AlarmRepo : JpaRepository<Alarm, Long> {

    fun findByTargetUserId(
        targetUserId: Long,
        pageable: Pageable
    ): Page<Alarm>
}