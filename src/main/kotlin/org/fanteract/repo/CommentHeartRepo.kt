package org.fanteract.repo

import org.fanteract.entity.CommentHeart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentHeartRepo: JpaRepository<CommentHeart, Long> {
}