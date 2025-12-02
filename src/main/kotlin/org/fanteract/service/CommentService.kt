package org.fanteract.service

import org.fanteract.domain.CommentHeartReader
import org.fanteract.domain.CommentHeartWriter
import org.fanteract.domain.CommentReader
import org.fanteract.domain.CommentWriter
import org.fanteract.domain.UserReader
import org.fanteract.domain.UserWriter
import org.fanteract.dto.*
import org.fanteract.enumerate.ActivePoint
import org.fanteract.enumerate.Balance
import org.fanteract.enumerate.FilterAction
import org.fanteract.filter.ProfanityFilterService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentWriter: CommentWriter,
    private val commentHeartReader: CommentHeartReader,
    private val commentHeartWriter: CommentHeartWriter,
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val profanityFilterService: ProfanityFilterService,
) {
    fun readCommentsByBoardId(
        boardId: Long,
        page: Int,
        size: Int
    ): ReadCommentListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val commentPage = commentReader.findByBoardId(boardId, pageable)
        val commentContent = commentPage.content

        val heartGroup = commentHeartReader.findByCommentIdIn(commentContent.map{it.commentId}).groupBy { it.commentId }
        val userMap = userReader.findByIdIn(commentContent.map{it.userId}).associateBy {it.userId }

        val payload =
            commentContent.map { comment ->
                val heart = heartGroup[comment.commentId]
                val user = userMap[comment.userId]!!

                ReadCommentResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    heartCount = heart?.count()?: 0,
                    userName = user.email,
                    createdAt = comment.createdAt!!,
                    updatedAt = comment.updatedAt!!,
                )
            }

        return ReadCommentListResponse(
            contents = payload,
            page = commentPage.number,
            size = commentPage.size,
            totalElements = commentPage.totalElements,
            totalPages = commentPage.totalPages,
            hasNext = commentPage.hasNext()
        )
    }

    fun readCommentsByUserId(
        userId: Long,
        page: Int,
        size: Int
    ): ReadCommentListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        )

        val commentPage = commentReader.findByUserId(userId, pageable)
        val commentContent = commentPage.content

        val heartGroup = commentHeartReader.findByCommentIdIn(commentContent.map{it.commentId}).groupBy { it.commentId }
        val userMap = userReader.findByIdIn(commentContent.map{it.userId}).associateBy {it.userId }

        val payload =
            commentContent.map { comment ->
                val heart = heartGroup[comment.commentId]
                val user = userMap[comment.userId]!!

                ReadCommentResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    heartCount = heart?.count()?: 0,
                    userName = user.email,
                    createdAt = comment.createdAt!!,
                    updatedAt = comment.updatedAt!!,
                )
            }

        return ReadCommentListResponse(
            contents = payload,
            page = commentPage.number,
            size = commentPage.size,
            totalElements = commentPage.totalElements,
            totalPages = commentPage.totalPages,
            hasNext = commentPage.hasNext()
        )
    }

    fun createComment(
        boardId: Long,
        userId: Long,
        createCommentRequest: CreateCommentRequest
    ): CreateCommentResponse {
        val user = userReader.findById(userId)

        if (user.balance < Balance.COMMENT.cost){
            throw IllegalArgumentException("비용이 부족합니다")
        }

        userWriter.updateBalance(userId, -Balance.COMMENT.cost)

        // 게시글 필터링 진행
        val filterAction =
            profanityFilterService.checkProfanityAndUpdateAbusePoint(
                userId = userId,
                text = createCommentRequest.content,
            )

        if (filterAction == FilterAction.BLOCK){
            return CreateCommentResponse(
                commentId = null,
                isFiltered = true
            )
        }

        // 코멘트 생성
        val comment =
            commentWriter.create(
                boardId = boardId,
                userId = userId,
                content = createCommentRequest.content,
            )

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = ActivePoint.COMMENT.point
        )

        return CreateCommentResponse(
            commentId = comment.commentId,
            isFiltered = false
        )
    }
    fun updateComment(commentId: Long, userId: Long, updateCommentRequest: UpdateCommentRequest) {
        val preComment = commentReader.readById(commentId)

        if (preComment.userId != userId) {
            throw NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다")
        }

        commentWriter.update(
            commentId = commentId,
            content = updateCommentRequest.content,
        )
    }
    fun deleteComment(commentId: Long, userId: Long) {
        val preComment = commentReader.readById(commentId)

        if (preComment.userId != userId) {
            throw NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다")
        }

        commentWriter.delete(commentId = commentId)
    }

    fun createHeartInComment(commentId: Long, userId: Long): CreateHeartInCommentResponse {
        // 비용 검증 및 차감
        val user = userReader.findById(userId)

        if (user.balance < Balance.HEART.cost){
            throw IllegalArgumentException("비용이 부족합니다")
        }
        
        userWriter.updateBalance(userId, -Balance.HEART.cost)
        
        // 하트 중복 및 코멘트 존재 여부 검증
        if (commentHeartReader.existsByUserIdAndCommentId(userId, commentId)) {
            throw NoSuchElementException("조건에 맞는 코멘트 좋아요 내용이 이미 존재합니다")
        }

        if (!commentReader.existsById(commentId)){
            throw NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다")
        }

        val response =
            commentHeartWriter.create(
                userId = userId,
                commentId = commentId,
            )

        // 활동 점수 변경
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = ActivePoint.HEART.point
        )

        return CreateHeartInCommentResponse(response.commentHeartId)
    }

    fun deleteHeartInComment(commentId: Long, userId: Long) {
        if (!commentReader.existsById(commentId)){
            throw NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다")
        }

        // 하트 삭제
        commentHeartWriter.delete(
            userId = userId,
            commentId = commentId,
        )

        // 활동 점수 반납
        userWriter.updateActivePoint(
            userId = userId,
            activePoint = -ActivePoint.HEART.point
        )
    }
}