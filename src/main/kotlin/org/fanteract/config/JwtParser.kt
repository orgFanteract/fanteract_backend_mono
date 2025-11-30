package org.fanteract.config

import jakarta.servlet.http.HttpServletRequest

class JwtParser {
    companion object {
        fun extractKey(request: HttpServletRequest, key: String): Long{
            val userId = request.getAttribute(key) as String

            return userId.toLong()
        }
    }
}
