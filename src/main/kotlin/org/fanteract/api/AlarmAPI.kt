package org.fanteract.api

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
import org.fanteract.dto.ReadAlarmListResponse
import org.fanteract.service.AlarmService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/alarms")
class AlarmAPI(
    private val alarmService: AlarmService,
) {
    @LoginRequired
    @Operation(summary = "사용자별 알람 조회")
    @GetMapping()
    fun readAlarmByUserId(
        request: HttpServletRequest,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
    ): ResponseEntity<ReadAlarmListResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = alarmService.readAlarmByUserId(userId, page, size)

        return ResponseEntity.ok().body(response)
    }
}