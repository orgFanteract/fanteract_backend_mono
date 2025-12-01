package org.fanteract.dto

data class CreateCommentRequest(
    val content: String,
)

data class UpdateCommentRequest(
    val content: String,
)