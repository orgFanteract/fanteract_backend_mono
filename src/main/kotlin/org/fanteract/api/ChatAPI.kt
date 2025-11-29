package org.fanteract.api

import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.dto.CreateChatroomRequest
import org.fanteract.dto.CreateChatroomResponse
import org.fanteract.dto.ReadChatroomListResponse
import org.fanteract.dto.ReadChatroomResponse
import org.fanteract.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chats")
class ChatAPI(
    private val chatService: ChatService,
) {
    @LoginRequired
    @PostMapping("/chatroom")
    fun createChatroom(
        request: HttpServletRequest,
        @RequestBody createChatroomRequest: CreateChatroomRequest,
        ): ResponseEntity<CreateChatroomResponse>{
        val userId = request.getAttribute("userId") as Long
        val response = chatService.createChatroom(userId, createChatroomRequest)

        return ResponseEntity.ok().body(response)
    }

    @LoginRequired
    @GetMapping("/chatroom")
    fun readChatroomListByUserId(
        request: HttpServletRequest,
    ): ResponseEntity<ReadChatroomListResponse>{
        val userId = request.getAttribute("userId") as Long
        val response = chatService.readChatroomListByUserId(userId)

        return ResponseEntity.ok().body(response)
    }

    @LoginRequired
    @GetMapping("{chatroomId}/chatroom")
    fun readChatroomById(
        request: HttpServletRequest,
        @PathVariable chatroomId: Long,
    ): ResponseEntity<ReadChatroomResponse>{
        val userId = request.getAttribute("userId") as Long
        val response = chatService.readChatroomById(userId, chatroomId)

        return ResponseEntity.ok().body(response)
    }
}