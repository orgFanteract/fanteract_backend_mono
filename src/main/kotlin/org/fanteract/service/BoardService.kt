package org.fanteract.service

import org.fanteract.domain.AlarmWriter
import org.fanteract.domain.BoardHeartReader
import org.fanteract.domain.BoardHeartWriter
import org.fanteract.domain.BoardReader
import org.fanteract.domain.BoardWriter
import org.fanteract.domain.CommentReader
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.CreateBoardRequest
import org.fanteract.dto.CreateBoardResponse
import org.fanteract.dto.CreateHeartInBoardResponse
import org.fanteract.dto.ReadBoardDetailResponse
import org.fanteract.dto.ReadBoardListResponse
import org.fanteract.dto.ReadBoardResponse
import org.fanteract.dto.UpdateBoardRequest
import org.fanteract.enumerate.ActivePoint
import org.fanteract.enumerate.AlarmStatus
import org.fanteract.enumerate.Balance
import org.fanteract.enumerate.ContentType
import org.fanteract.enumerate.RiskLevel
import org.fanteract.filter.ProfanityFilterService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.Long

@Transactional
@Service
class BoardService(
    private val boardReader: BoardReader,
    private val boardWriter: BoardWriter,
    private val commentReader: CommentReader,
    private val boardHeartReader: BoardHeartReader,
    private val boardHeartWriter: BoardHeartWriter,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val alarmWriter: AlarmWriter,
    private val profanityFilterService: ProfanityFilterService,
) {
    fun createBoard(
        createBoardRequest: CreateBoardRequest,
        userId: Long
    ): CreateBoardResponse {
        // 사용자 잔여 포인트 확인
        val user = userReader.findById(userId)
        
        if (user.balance < Balance.BOARD.cost){
            throw IllegalArgumentException("비용이 부족합니다")
        }

        userWriter.updateBalance(userId, -Balance.BOARD.cost)
        
        // 게시글 필터링 진행
        val riskLevel =
            profanityFilterService.checkProfanityAndUpdateAbusePoint(
                userId = userId,
                text = "${createBoardRequest.title}\n${createBoardRequest.content}",
            )

        // 게시글 생성
        val board =
            boardWriter.create(
                title = createBoardRequest.title,
                content = createBoardRequest.content,
                userId = userId,
                riskLevel = riskLevel,
            )

        // 활동 점수 변경
        if (riskLevel != RiskLevel.BLOCK) {
            userWriter.updateActivePoint(
                userId = userId,
                activePoint = ActivePoint.BOARD.point
            )
        }

        return CreateBoardResponse(
            boardId = board.boardId,
            riskLevel = riskLevel,
        )
    }

    fun readBoardByUserId(page: Int, size: Int, userId: Long): ReadBoardListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val boardPage = boardReader.readByUserId(pageable, userId)
        val boardContent = boardPage.content

        val commentGroup =
            commentReader
                .findByBoardIdIn(boardContent.map{it.boardId})
                .groupBy { it.boardId }

        val heartGroup =
            boardHeartReader
                .findByBoardIdIn(boardContent.map{it.boardId})
                .groupBy {it.boardId }

        val userMap =
            userReader.findByIdIn(boardContent.map{it.userId}).associateBy { it.userId }


        val payload =
            boardContent.map{ board ->
                val comment = commentGroup[board.boardId]
                val heart = heartGroup[board.boardId]
                val user = userMap[board.userId]

                ReadBoardResponse(
                    boardId = board.boardId,
                    title = board.title,
                    userName = user?.name ?: "-",
                    commentCount = comment?.count() ?: 0,
                    heartCount = heart?.count() ?: 0,
                    createdAt = board.createdAt!!,
                    updatedAt = board.updatedAt!!,
                )
            }

        return ReadBoardListResponse(
            contents = payload,
            page = page,
            size = size,
            totalElements = boardPage.totalElements,
            totalPages = boardPage.totalPages,
            hasNext = boardPage.hasNext()
        )
    }

    fun readBoard(page: Int, size: Int): ReadBoardListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val boardPage =
            boardReader.findAll(
                pageable = pageable
            )

        val boardContent = boardPage.content

        val commentGroup =
            commentReader
                .findByBoardIdIn(boardContent.map{it.boardId})
                .groupBy { it.boardId }

        val heartGroup =
            boardHeartReader
                .findByBoardIdIn(boardContent.map{it.boardId})
                .groupBy {it.boardId }

        val userMap =
            userReader.findByIdIn(boardContent.map{it.userId}).associateBy { it.userId }

        val payload =
            boardContent.map{ board ->
                val comment = commentGroup[board.boardId]
                val heart = heartGroup[board.boardId]
                val user = userMap[board.userId]

                ReadBoardResponse(
                    boardId = board.boardId,
                    title = board.title,
                    userName = user?.name ?: "-",
                    commentCount = comment?.count() ?: 0,
                    heartCount = heart?.count() ?: 0,
                    createdAt = board.createdAt!!,
                    updatedAt = board.updatedAt!!,
                )
            }


        return ReadBoardListResponse(
            contents = payload,
            page = page,
            size = size,
            totalElements = boardPage.totalElements,
            totalPages = boardPage.totalPages,
            hasNext = boardPage.hasNext()
        )
    }

    fun readBoardDetail(
        boardId: Long
    ): ReadBoardDetailResponse {
        val board = boardReader.readById(boardId)

        val commentList = commentReader.findByBoardIdIn(listOf(board.boardId))
        val heartList = boardHeartReader.findByBoardIdIn(listOf(board.boardId))
        val user = userReader.findById(board.userId)

        return ReadBoardDetailResponse(
            boardId = board.boardId,
            title = board.title,
            content = board.content,
            userName = user.name,
            commentCount = commentList.count(),
            heartCount = heartList.count(),
            createdAt = board.createdAt!!,
            updatedAt = board.updatedAt!!,
        )
    }

    fun updateBoard(
        boardId: Long,
        userId: Long,
        updateBoardRequest: UpdateBoardRequest
    ) {
        val preBoard = boardReader.readById(boardId)

        if (preBoard.userId != userId){
            throw NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")
        }

        boardWriter
            .update(
                boardId = boardId,
                title = updateBoardRequest.title,
                content = updateBoardRequest.content,
            )
    }

    fun createHeartInBoard(boardId: Long, userId: Long): CreateHeartInBoardResponse{
        // 비용 검증 및 차감
        val user = userReader.findById(userId)

        if (user.balance < Balance.HEART.cost){
            throw IllegalArgumentException("비용이 부족합니다")
        }

        userWriter.updateBalance(userId, -Balance.HEART.cost)
        
        // 존재 여부 검증
        if (boardHeartReader.existsByUserIdAndBoardId(userId, boardId)){
            throw NoSuchElementException("조건에 맞는 게시글 좋아요 내용이 이미 존재합니다")
        }

        if (!boardReader.existsById(boardId)){
            throw NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")
        }

        val boardHeart =
            boardHeartWriter.create(
                userId = userId,
                boardId = boardId,
            )

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = ActivePoint.HEART.point
        )

        val board = boardReader.readById(boardHeart.boardId)

        // 알림 전송
        alarmWriter.create(
            userId = userId,
            targetUserId = board.userId,
            contentType = ContentType.BOARD_HEART,
            contentId = boardHeart.boardHeartId,
            alarmStatus = AlarmStatus.CREATED,
        )

        return CreateHeartInBoardResponse(boardHeart.boardHeartId)
    }

    fun deleteHeartInBoard(boardId: Long, userId: Long) {
        if (!boardReader.existsById(boardId)){
            throw NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")
        }

        // 하트 해제
        boardHeartWriter.delete(
            userId = userId,
            boardId = boardId,
        )

        // 활동 점수 반납
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = -ActivePoint.HEART.point
        )
    }
}