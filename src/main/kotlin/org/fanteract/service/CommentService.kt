package org.fanteract.service

import org.fanteract.domain.CommentReader
import org.fanteract.domain.CommentWriter
import org.fanteract.dto.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentWriter: CommentWriter,
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

        val payload =
            commentContent.map { comment ->
                ReadCommentResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    userId = comment.userId,
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

        val payload =
            commentContent.map { comment ->
                ReadCommentResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    userId = comment.userId,
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

    fun createComment(boardId: Long, userId: Long, createCommentRequest: CreateCommentRequest): CreateCommentResponse {
        val comment =
            commentWriter.create(
                boardId = boardId,
                userId = userId,
                content = createCommentRequest.content,
            )

        return CreateCommentResponse(commentId = comment.commentId)
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
}