package org.fanteract.service

import org.fanteract.domain.ChatReader
import org.fanteract.domain.ChatWriter
import org.fanteract.domain.ChatroomReader
import org.fanteract.domain.ChatroomWriter
import org.fanteract.domain.UserChatroomHistoryWriter
import org.fanteract.domain.UserChatroomReader
import org.fanteract.domain.UserChatroomWriter
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.CreateChatroomRequest
import org.fanteract.dto.CreateChatroomResponse
import org.fanteract.dto.CreateCommentResponse
import org.fanteract.dto.JoinChatroomResponse
import org.fanteract.dto.LeaveChatroomResponseDto
import org.fanteract.dto.ReadChatContainingListResponse
import org.fanteract.dto.ReadChatContainingRequest
import org.fanteract.dto.ReadChatContainingResponse
import org.fanteract.dto.ReadChatListResponse
import org.fanteract.dto.ReadChatResponse
import org.fanteract.dto.ReadChatroomListResponse
import org.fanteract.dto.ReadChatroomResponse
import org.fanteract.dto.SendChatRequestDto
import org.fanteract.dto.SendChatResponse
import org.fanteract.entity.UserChatroom
import org.fanteract.enumerate.ActivePoint
import org.fanteract.enumerate.Balance
import org.fanteract.enumerate.ChatroomJoinStatus
import org.fanteract.enumerate.FilterAction
import org.fanteract.filter.ProfanityFilterService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.Long

@Transactional
@Service
class ChatService(
    private val chatroomReader: ChatroomReader,
    private val chatroomWriter: ChatroomWriter,
    private val userChatroomReader: UserChatroomReader,
    private val userChatroomWriter: UserChatroomWriter,
    private val userChatroomHistoryWriter: UserChatroomHistoryWriter,
    private val chatWriter: ChatWriter,
    private val chatReader: ChatReader,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val profanityFilterService: ProfanityFilterService
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

        // TODO: 해당 채팅방에 참여 상태로 변경


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
    fun readChatroomSummaryById(
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

    fun joinChatroom(
        userId: Long,
        chatroomId: Long
    ): JoinChatroomResponse {
        // 채팅방 존재여부 확인
        chatroomReader.existsById(chatroomId)

        // 채팅방 접속기록 확인
        val preUserChatroom = userChatroomReader.findByUserIdAndChatroomId(userId, chatroomId)

        // 접속 기록이 존재하는지 확인
        if (preUserChatroom != null && preUserChatroom.chatroomJoinStatus == ChatroomJoinStatus.JOIN){
            throw NoSuchElementException("이미 참여중인 채팅방입니다")
        }

        // 채팅방 입장 기록 생성
        val userChatroom =
            joinUserChatroom(
                preUserChatroom = preUserChatroom,
                userId = userId,
                chatroomId = chatroomId,
            )

        return JoinChatroomResponse(userChatroom.userChatroomId)
    }
    fun leaveChatroom(
        userId: Long,
        chatroomId: Long
    ): LeaveChatroomResponseDto {
        // 채팅방 존재여부 확인
        chatroomReader.existsById(chatroomId)

        // 채팅방 접속기록 확인
        val preUserChatroom = userChatroomReader.findByUserIdAndChatroomId(userId, chatroomId)

        // 접속 기록이 존재하는지 확인
        if (preUserChatroom == null || preUserChatroom.chatroomJoinStatus == ChatroomJoinStatus.LEAVE){
            throw NoSuchElementException("이미 탈퇴했거나 입장하지 않은 채팅방입니다")
        }

        // 채팅방 입장 기록 변경
        val userChatroom =
            leaveUserChatroom(
                preUserChatroom = preUserChatroom,
                userId = preUserChatroom.userId,
                chatroomId = preUserChatroom.chatroomId,
            )

        return LeaveChatroomResponseDto(userChatroom.userChatroomId)
    }
    fun sendChat(
        sendChatRequestDto: SendChatRequestDto,
        chatroomId: Long,
        userId: Long
    ): SendChatResponse {
        // 비용 검증 및 차감
        val user = userReader.findById(userId)
        
        if (user.balance < Balance.CHAT.cost){
            throw IllegalArgumentException("비용이 부족합니다")
        }

        userWriter.updateBalance(userId, -Balance.CHAT.cost)
        
        // 게시글 필터링 진행
        val filterAction =
            profanityFilterService.checkProfanityAndUpdateAbusePoint(
                userId = userId,
                text = sendChatRequestDto.content,
            )

        if (filterAction == FilterAction.BLOCK){
            return SendChatResponse(
                isFiltered = true
            )
        }

        val chat =
            chatWriter.create(
                content = sendChatRequestDto.content,
                chatroomId = chatroomId,
                userId = userId,
            )
        

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = ActivePoint.CHAT.point
        )

        return SendChatResponse(
            chatId = chat.chatId,
            userName = user.name,
            content = chat.content,
            createdAt = chat.createdAt!!,
            isFiltered = false
        )
    }

    fun leaveUserChatroom(
        preUserChatroom: UserChatroom,
        userId: Long,
        chatroomId: Long,
    ): UserChatroom {
        val userChatroom =
            userChatroomWriter.update(
                userChatroomId = preUserChatroom.userChatroomId,
                userId = preUserChatroom.userId,
                chatroomId = preUserChatroom.chatroomId,
                chatroomJoinStatus = ChatroomJoinStatus.LEAVE
            )

        userChatroomHistoryWriter.create(
            userId = userId,
            chatroomId = chatroomId,
            chatroomJoinStatus = ChatroomJoinStatus.LEAVE
        )

        return userChatroom
    }
    fun joinUserChatroom(
        preUserChatroom: UserChatroom?,
        userId: Long,
        chatroomId: Long,
    ): UserChatroom {
        val userChatroom =
            if (preUserChatroom == null) {
                // 입장한 경험이 없는 경우
                userChatroomWriter.create(
                    userId = userId,
                    chatroomId = chatroomId,
                    chatroomJoinStatus = ChatroomJoinStatus.JOIN
                )
            } else {
                // 입장 후 떠난 상태인 경우
                userChatroomWriter.update(
                    userChatroomId = preUserChatroom.userChatroomId,
                    userId = preUserChatroom.userId,
                    chatroomId = preUserChatroom.chatroomId,
                    chatroomJoinStatus = ChatroomJoinStatus.JOIN
                )
            }

        // history 기록
        userChatroomHistoryWriter.create(
            userId = userId,
            chatroomId = chatroomId,
            chatroomJoinStatus = ChatroomJoinStatus.JOIN
        )

        return userChatroom
    }

    @Transactional(readOnly = true)
    fun readChatByChatroomId(
        userId: Long,
        chatroomId: Long,
        page: Int,
        size: Int
    ): ReadChatListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val chatPage = chatReader.findByUserIdAndChatroomId(
            userId = userId,
            chatroomId = chatroomId,
            pageable = pageable
        )

        val userMap = userReader.findByIdIn(chatPage.content.map{it.userId}).associateBy {it.userId }

        val contents = chatPage.content.map { chat ->
            val user = userMap[chat.userId]
            ReadChatResponse(
                chatId = chat.chatId,
                userName = user?.name ?: "-",
                content = chat.content,
                createdAt = chat.createdAt!!
            )
        }

        return ReadChatListResponse(
            contents = contents,
            page = chatPage.number,
            size = chatPage.size,
            totalElements = chatPage.totalElements,
            totalPages = chatPage.totalPages,
            hasNext = chatPage.hasNext()
        )
    }

    fun readChatContainingByChatroomId(
        userId: Long,
        chatroomId: Long,
        readChatContainingRequest: ReadChatContainingRequest,
        page: Int,
        size: Int
    ): ReadChatContainingListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val chatPage = chatReader.findByUserIdAndChatroomIdAnd(
            userId = userId,
            chatroomId = chatroomId,
            content = readChatContainingRequest.content,
            pageable = pageable
        )

        val userMap = userReader.findByIdIn(chatPage.content.map{it.userId}).associateBy {it.userId }

        val contents = chatPage.content.map { chat ->
            val user = userMap[chat.userId]
            ReadChatContainingResponse(
                chatId = chat.chatId,
                userName = user?.name ?: "-",
                content = chat.content,
                createdAt = chat.createdAt!!
            )
        }

        return ReadChatContainingListResponse(
            contents = contents,
            page = chatPage.number,
            size = chatPage.size,
            totalElements = chatPage.totalElements,
            totalPages = chatPage.totalPages,
            hasNext = chatPage.hasNext()
        )
    }

    fun readChatroomListByUserIdAndTitleContaining(
        userId: Long,
        title: String
    ): ReadChatroomListResponse {
        val chatroomList = chatroomReader.findByUserIdAndTitleContaining(userId, title)

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
}