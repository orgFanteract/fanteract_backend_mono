package org.fanteract.dto

data class CreateBoardRequest(
    val title: String,
    val content: String,
)

data class UpdateBoardRequest(
    val title: String,
    val content: String,
)
