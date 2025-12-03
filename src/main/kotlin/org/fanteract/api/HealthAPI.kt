package org.fanteract.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthAPI {
    @GetMapping()
    fun healthCheck(): ResponseEntity<String>{
        return ResponseEntity.ok().body("I'm healthy")
    }
}