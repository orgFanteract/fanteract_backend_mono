package org.fanteract.dto

data class UserSignInRequestDto(
    val email: String,
    val password: String,
)

data class UserSignUpRequestDto(
    val email: String,
    val password: String,
)