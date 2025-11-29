package org.fanteract.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fanteract.entity.constant.BaseEntity
import org.fanteract.enumerate.AlarmStatus

@Entity
@Table(name = "alarms")
class Alarm (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val alarmId: Long,
    val userId: Long,
    val contentId: Long,
    @Enumerated(EnumType.STRING)
    val alarmStatus: AlarmStatus,
): BaseEntity()