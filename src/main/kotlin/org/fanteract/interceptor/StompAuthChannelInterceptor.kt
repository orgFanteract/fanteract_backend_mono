package org.fanteract.interceptor

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import java.security.Principal
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import java.util.concurrent.ConcurrentHashMap

@Component
class StompAuthChannelInterceptor(
    @Value("\${jwt.secret}") private val jwtSecret: String,
) : ChannelInterceptor {
    private val sessionPrincipalMap = ConcurrentHashMap<String, Principal>()

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel
    ): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)
        val command = accessor.command
        val sessionId = accessor.sessionId

        when (command) {
            StompCommand.CONNECT -> {
                val authHeader = accessor.getFirstNativeHeader("Authorization")
                    ?: throw IllegalArgumentException("No Authorization header in STOMP CONNECT")

                if (!authHeader.startsWith("Bearer ")) {
                    throw IllegalArgumentException("Invalid Authorization header in STOMP CONNECT")
                }

                val token = authHeader.removePrefix("Bearer ").trim()
                val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

                val subject = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).payload.subject

                println("CONNECT subject : $subject, sessionId=$sessionId")

                val principal = Principal { subject }

                // 1) 이 메시지에 user 세팅
                accessor.user = principal
                // 2) 세션에 매핑 저장
                if (sessionId != null) {
                    sessionPrincipalMap[sessionId] = principal
                }
            }

            StompCommand.DISCONNECT -> {
                if (sessionId != null) {
                    sessionPrincipalMap.remove(sessionId)
                    println("DISCONNECT sessionId=$sessionId -> principal removed")
                }
            }

            else -> {
                // SUBSCRIBE, SEND 등에서 user가 비어 있으면 우리가 세션에서 복구
                if (sessionId != null && accessor.user == null) {
                    val principal = sessionPrincipalMap[sessionId]
                    if (principal != null) {
                        accessor.user = principal
                        println("RESTORE principal for command=$command, sessionId=$sessionId, user=${principal.name}")
                    } else {
                        println("NO principal stored for command=$command, sessionId=$sessionId")
                    }
                }
            }
        }

        println("DEBUG command=$command, headers=${accessor.messageHeaders}")

        return MessageBuilder.createMessage(message.payload, accessor.messageHeaders)
    }
}