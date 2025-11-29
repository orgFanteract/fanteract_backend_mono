package org.fanteract.api

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.fanteract.annotation.LoginRequired
import org.fanteract.dto.*
import org.fanteract.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
    ): ResponseEntity<Void>{
        userService.signIn(userSignInRequestDto)

        return ResponseEntity.ok().build()
    }


    @PostMapping("/sign-up")
    fun signUpWithCredential(
        @RequestBody userSignUpRequestDto: UserSignUpRequestDto,
    ): ResponseEntity<UserSignUpResponseDto> {
        val response = userService.signUp(userSignUpRequestDto)


        return ResponseEntity.ok(response)
    }
}