package org.fanteract.api

import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
import org.fanteract.dto.*
import org.fanteract.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

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

    // 특정 채팅방 접속
    @LoginRequired
    @PostMapping("/{chatroomId}/join")
    fun joinChatroom(
        request: HttpServletRequest,
        @PathVariable chatroomId: Long,
    ): ResponseEntity<JoinChatroomResponseDto>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = chatService.joinChatroom(userId, chatroomId)

        return ResponseEntity.ok().body(response)
    }

    // 특정 채팅방 탈퇴
    @LoginRequired
    @PostMapping("/{chatroomId}/leave")
    fun leaveChatroom(
        request: HttpServletRequest,
        @PathVariable chatroomId: Long,
    ): ResponseEntity<LeaveChatroomResponseDto>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = chatService.leaveChatroom(userId, chatroomId)

        return ResponseEntity.ok().body(response)
    }

    // 특정 채팅방에 채팅 전송
    @MessageMapping("/chat.{chatroomId}") // 다음 url path를 통해 발동
    @SendTo("/subscribe/chat.{chatroomId}") // 해당 결과는 다음 path를 구독하는 클라이언트에게 전달
    fun sendChat(
        principal: Principal,
        sendChatRequestDto: SendChatRequestDto,
        @DestinationVariable chatroomId: Long
    ): SendChatResponseDto{
//        if (principal == null){
//            throw NoSuchElementException("조건에 맞는 토큰이 존재하지 않습니다")
//        }
        val userId = principal.name.toLong()
        println("userId: $userId chatroomId: $chatroomId content:${sendChatRequestDto.content}")
        val response = chatService.sendChat(sendChatRequestDto, chatroomId, userId)

        return response
    }
}