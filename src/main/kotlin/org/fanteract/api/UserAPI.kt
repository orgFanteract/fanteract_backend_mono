package org.fanteract.api

import org.fanteract.dto.*
import org.fanteract.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserAPI(
    private val userService: UserService,
    @Value("\${jwt.secret}") private val jwtSecret: String,
) {
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody userSignInRequestDto: UserSignInRequestDto
    ): ResponseEntity<UserSignInResponseDto>{
        val response = userService.signIn(userSignInRequestDto)

        return ResponseEntity.ok(response)
    }


    @PostMapping("/sign-up")
    fun signUpWithCredential(
        @RequestBody userSignUpRequestDto: UserSignUpRequestDto,
    ): ResponseEntity<Void> {
        userService.signUp(userSignUpRequestDto)

        return ResponseEntity.ok().build()
    }
}