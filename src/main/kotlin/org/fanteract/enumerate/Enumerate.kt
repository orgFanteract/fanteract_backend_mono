package org.fanteract.enumerate

enum class Status {
    ACTIVATED,
    DELETED,
}

enum class AlarmStatus {
    CREATED,
    DELETED,
    UPDATED,
}

enum class ChatroomJoinStatus{
    JOIN,
    LEAVE,
}

enum class FilterAction {
    ALLOW,     // 그냥 통과
    WARN,      // 소프트 경고 (예: 저장은 하되, 나중에 신고/리뷰 후보)
    BLOCK,     // 저장/전송 자체 금지
}

enum class ActivePoint(
    val point: Int,   // ← 생성자 파라미터 정의
) {
    BOARD(10),
    COMMENT(3),
    CHAT(1),
    HEART(1),
}