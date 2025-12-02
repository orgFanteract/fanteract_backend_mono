package org.fanteract.dto

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.fanteract.enumerate.AlarmStatus
import org.fanteract.enumerate.ContentType

data class ReadAlarmListResponse(
    val contents: List<ReadAlarmResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean,
)

data class ReadAlarmResponse(
    val alarmId: Long,
    val userId: Long,
    val targetUserId: Long,
    val contentId: Long,
    val contentType: ContentType,
    val alarmStatus: AlarmStatus,
)