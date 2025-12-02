package org.fanteract.domain

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.fanteract.entity.Alarm
import org.fanteract.enumerate.AlarmStatus
import org.fanteract.enumerate.ContentType
import org.fanteract.repo.AlarmRepo
import org.springframework.stereotype.Component
import kotlin.Long

@Component
class AlarmWriter(
    private val alarmRepo: AlarmRepo,
) {
    fun create(
        userId: Long,
        targetUserId: Long,
        contentType: ContentType,
        contentId: Long,
        alarmStatus: AlarmStatus,
    ): Alarm {
        return alarmRepo.save(
                Alarm(
                    userId = userId,
                    targetUserId = targetUserId,
                    contentId = contentId,
                    contentType = contentType,
                    alarmStatus = alarmStatus,
                )
            )
    }

}