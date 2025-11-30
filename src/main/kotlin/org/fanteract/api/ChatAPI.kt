package org.fanteract.api

import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
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
    // 채팅방 생성
    @LoginRequired
    @PostMapping("/chatroom")
    fun createChatroom(
        request: HttpServletRequest,
        @RequestBody createChatroomRequest: CreateChatroomRequest,
        ): ResponseEntity<CreateChatroomResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = chatService.createChatroom(userId, createChatroomRequest)

        return ResponseEntity.ok().body(response)
    }

    // 유저 기반 채팅방 조회
    @LoginRequired
    @GetMapping("/chatroom")
    fun readChatroomListByUserId(
        request: HttpServletRequest,
    ): ResponseEntity<ReadChatroomListResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = chatService.readChatroomListByUserId(userId)

        return ResponseEntity.ok().body(response)
    }

    // 특정 채팅방 조회
    @LoginRequired
    @GetMapping("{chatroomId}/chatroom")
    fun readChatroomById(
        request: HttpServletRequest,
        @PathVariable chatroomId: Long,
    ): ResponseEntity<ReadChatroomResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = chatService.readChatroomById(userId, chatroomId)

        return ResponseEntity.ok().body(response)
    }
}