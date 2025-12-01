package org.fanteract.domain

import org.fanteract.entity.CommentHeart
import org.fanteract.repo.CommentHeartRepo
import org.springframework.stereotype.Component

@Component
class CommentHeartReader(
    private val commentHeartRepo: CommentHeartRepo,
) {
    fun findByCommentIdIn(idList: List<Long>): List<CommentHeart> {
        return commentHeartRepo.findByCommentIdIn(idList)
    }

    fun existsByUserIdAndCommentId(userId: Long, commentId: Long): Boolean {
        return commentHeartRepo.existsByUserIdAndCommentId(userId, commentId)
    }
}