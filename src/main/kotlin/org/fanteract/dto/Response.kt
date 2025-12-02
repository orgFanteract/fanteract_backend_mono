package org.fanteract.dto

import org.fanteract.enumerate.FilterAction

data class FilterResult(
    val action: FilterAction,
    val reason: String? = null,
    val score: Double? = null, // ML 토크시티 점수 등
)