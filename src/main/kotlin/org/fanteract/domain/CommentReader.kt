package org.fanteract.domain

import org.fanteract.entity.Comment
import org.fanteract.enumerate.Status
import org.fanteract.repo.CommentRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepo: CommentRepo,
) {
    fun findByBoardIdIn(idList: List<Long>): List<Comment> {
        return commentRepo.findByBoardIdIn(idList)
    }

    fun findByBoardId(boardId: Long, pageable: Pageable): Page<Comment> {
        return commentRepo.findByBoardId(boardId, pageable)
    }

    fun findByUserId(userId: Long, pageable: Pageable): Page<Comment> {
        return commentRepo.findByUserId(userId, pageable)
    }

    fun readById(commentId: Long): Comment {
        val comment = commentRepo.findById(commentId)
            .orElseThrow { NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다") }

        if (comment.status == Status.DELETED)
            throw NoSuchElementException("조건에 맞는 코멘트가 존재하지 않습니다")

        return comment
    }
}