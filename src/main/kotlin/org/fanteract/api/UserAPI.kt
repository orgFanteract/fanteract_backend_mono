package org.fanteract.api

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
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
) {
    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody userSignInRequestDto: UserSignInRequestDto
    ): ResponseEntity<UserSignInResponseDto>{
        val response = userService.signIn(userSignInRequestDto)

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    fun signUpWithCredential(
        @RequestBody userSignUpRequestDto: UserSignUpRequestDto,
    ): ResponseEntity<Void> {
        userService.signUp(userSignUpRequestDto)

        return ResponseEntity.ok().build()
    }

    @LoginRequired
    @Operation(summary = "마이페이지 조회")
    @GetMapping("/my-page")
    fun readMyPage(
        request: HttpServletRequest
    ): ResponseEntity<ReadMyPageResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = userService.readMyPage(userId)
    }
}