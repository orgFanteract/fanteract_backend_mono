package org.fanteract.domain

import org.fanteract.entity.Comment
import org.fanteract.repo.CommentRepo
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepo: CommentRepo,
) {
    fun findByBoardIdIn(idList: List<Long>): List<Comment> {
        return commentRepo.findByBoardIdIn(idList)
    }
}