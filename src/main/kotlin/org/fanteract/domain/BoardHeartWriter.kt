package org.fanteract.domain

import org.fanteract.repo.BoardHeartRepo
import org.springframework.stereotype.Component

@Component
class BoardHeartWriter(
    private val boardHeartRepo: BoardHeartRepo,
) {
}