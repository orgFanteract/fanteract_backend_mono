package org.fanteract.repo

import org.fanteract.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepo: JpaRepository<Chat, Long>{}