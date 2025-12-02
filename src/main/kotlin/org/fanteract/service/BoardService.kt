package org.fanteract.service

import jakarta.persistence.Column
import org.fanteract.domain.BoardHeartReader
import org.fanteract.domain.BoardHeartWriter
import org.fanteract.domain.BoardReader
import org.fanteract.domain.BoardWriter
import org.fanteract.domain.CommentReader
import org.fanteract.domain.CommentWriter
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.CreateBoardRequest
import org.fanteract.dto.CreateBoardResponse
import org.fanteract.dto.CreateHeartInBoardResponse
import org.fanteract.dto.ReadBoardDetailResponse
import org.fanteract.dto.ReadBoardListResponse
import org.fanteract.dto.ReadBoardResponse
import org.fanteract.dto.UpdateBoardRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.Long
import kotlin.String

@Service
class BoardService(
    private val boardReader: BoardReader,
    private val boardWriter: BoardWriter,
    private val commentReader: CommentReader,
    private val boardHeartReader: BoardHeartReader,
    private val boardHeartWriter: BoardHeartWriter,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
) {
    fun createBoard(
        createBoardRequest: CreateBoardRequest,
        userId: Long
    ): CreateBoardResponse {
        val board =
            boardWriter.create(
                title = createBoardRequest.title,
                content = createBoardRequest.content,
                userId = userId,
            )

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = 10
        )

        return CreateBoardResponse(board.boardId)
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
        if (boardHeartReader.existsByUserIdAndBoardId(userId, boardId)){
            throw NoSuchElementException("조건에 맞는 게시글 좋아요 내용이 이미 존재합니다")
        }

        if (!boardReader.existsById(boardId)){
            throw NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")
        }

        val response =
            boardHeartWriter.create(
                userId = userId,
                boardId = boardId,
            )

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = 1
        )

        return CreateHeartInBoardResponse(response.boardHeartId)
    }

    fun deleteHeartInBoard(boardId: Long, userId: Long) {
        if (!boardReader.existsById(boardId)){
            throw NoSuchElementException("조건에 맞는 게시글이 존재하지 않습니다")
        }

        boardHeartWriter.delete(
            userId = userId,
            boardId = boardId,
        )
    }
}