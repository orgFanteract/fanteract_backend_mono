package org.fanteract.service

import org.fanteract.repo.UserRepo
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
) {

}