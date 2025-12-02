package org.fanteract.service

import org.fanteract.domain.AlarmReader
import org.fanteract.dto.ReadAlarmListResponse
import org.fanteract.dto.ReadAlarmResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AlarmService(
    private val alarmReader: AlarmReader,
) {
    fun readAlarmByUserId(
        targetUserId: Long,
        page: Int,
        size: Int,
    ): ReadAlarmListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt") // 최신 알람부터
        )

        val alarmPage = alarmReader.findByTargetUserId(targetUserId, pageable)
        val alarmContent = alarmPage.content

        val contents = alarmContent.map { alarm ->
            ReadAlarmResponse(
                alarmId = alarm.alarmId,
                userId = alarm.userId,
                targetUserId = alarm.targetUserId,
                contentId = alarm.contentId,
                contentType = alarm.contentType,
                alarmStatus = alarm.alarmStatus,
            )
        }

        return ReadAlarmListResponse(
            contents = contents,
            page = page,
            size = size,
            totalElements = alarmPage.totalElements,
            totalPages = alarmPage.totalPages,
            hasNext = alarmPage.hasNext()
        )
    }

}