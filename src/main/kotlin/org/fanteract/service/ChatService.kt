package org.fanteract.service

import org.fanteract.domain.ChatReader
import org.fanteract.domain.ChatWriter
import org.fanteract.domain.ChatroomReader
import org.fanteract.domain.ChatroomWriter
import org.fanteract.dto.CreateChatroomRequest
import org.fanteract.dto.CreateChatroomResponse
import org.fanteract.dto.ReadChatroomListResponse
import org.fanteract.dto.ReadChatroomResponse
import org.springframework.stereotype.Service
import kotlin.Long

@Service
class ChatService(
    private val chatroomReader: ChatroomReader,
    private val chatroomWriter: ChatroomWriter,
) {
    fun createChatroom(
        userId: Long,
        createChatroomRequest: CreateChatroomRequest
    ): CreateChatroomResponse {
        val chatroom =
            chatroomWriter.create(
                title = createChatroomRequest.title,
                description = createChatroomRequest.description,
                userId = userId,
            )

        return CreateChatroomResponse(
            chatroomId = chatroom.chatroomId,
        )
    }

    fun readChatroomListByUserId(
        userId: Long
    ):ReadChatroomListResponse {
        val chatroomList = chatroomReader.findByUserId(userId)

        return ReadChatroomListResponse(
            chatroomList.map{
                ReadChatroomResponse(
                    chatroomId = it.chatroomId,
                    title = it.title,
                    description = it.description,
                )
            }
        )
    }
    fun readChatroomById(
        userId: Long,
        chatroomId: Long,
    ): ReadChatroomResponse {
        val chatroom = chatroomReader.findByChatroomIdAndUserId(chatroomId, userId)

        return ReadChatroomResponse(
            chatroomId = chatroom.chatroomId,
            title = chatroom.title,
            description = chatroom.description,
        )
    }

}