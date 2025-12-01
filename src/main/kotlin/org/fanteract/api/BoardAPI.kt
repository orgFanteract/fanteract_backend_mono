package org.fanteract.api

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
import org.fanteract.dto.*
import org.fanteract.service.BoardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards")
class BoardAPI(
    private val boardService: BoardService,
) {
    @LoginRequired
    @Operation(summary = "게시글 생성")
    @PostMapping()
    fun createBoard(
        request: HttpServletRequest,
        @RequestBody createBoardRequest: CreateBoardRequest,
    ): ResponseEntity<CreateBoardResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = boardService.createBoard(createBoardRequest, userId)

        return ResponseEntity.ok().body(response)
    }

    // 사용자 작성 게시글 조회
    @LoginRequired
    @Operation(summary = "사용자 소유 게시글 조회")
    @GetMapping("/user")
    fun readBoardByUserId(
        request: HttpServletRequest,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
    ): ResponseEntity<ReadBoardListResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = boardService.readBoardByUserId(page, size, userId)

        return ResponseEntity.ok().body(response)
    }

    // 전체 게시글 조회
    @LoginRequired
    @Operation(summary = "전체 게시글 조회")
    @GetMapping("")
    fun readBoard(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
    ): ResponseEntity<ReadBoardListResponse>{
        val response = boardService.readBoard(
            page = page,
            size = size
        )

        return ResponseEntity.ok().body(response)
    }

    // 특정 게시글 상세 조회
    @LoginRequired
    @Operation(summary = "특정 게시글 상세 조회")
    @GetMapping("/{boardId}/board")
    fun readBoardDetail(
        @PathVariable boardId: Long,
    ): ResponseEntity<ReadBoardDetailResponse>{
        val response = boardService.readBoardDetail(boardId)

        return ResponseEntity.ok().body(response)
    }

    // 게시글 수정
    @LoginRequired
    @Operation(summary = "게시글 수정")
    @PutMapping("/{boardId}")
    fun updateBoard(
        request: HttpServletRequest,
        @PathVariable boardId: Long,
        @RequestBody updateBoardRequest: UpdateBoardRequest
    ): ResponseEntity<Void>{
        val userId = JwtParser.extractKey(request, "userId")
        boardService.updateBoard(boardId, userId, updateBoardRequest)

        return ResponseEntity.ok().build()
    }

    // 게시글 좋아요 선택
    @LoginRequired
    @Operation(summary = "게시글 좋아요 생성")
    @PostMapping("/{boardId}/heart")
    fun createHeartInBoard(
        request: HttpServletRequest,
        @PathVariable boardId: Long,
    ): ResponseEntity<CreateHeartInBoardResponse>{
        val userId = JwtParser.extractKey(request, "userId")
        val response = boardService.createHeartInBoard(boardId, userId)

        return ResponseEntity.ok().body(response)
    }

    // 게시글 좋아요 취소
    @LoginRequired
    @Operation(summary = "게시글 좋아요 해제")
    @DeleteMapping("/{boardId}/heart")
    fun deleteHeartInBoard(
        request: HttpServletRequest,
        @PathVariable boardId: Long,
    ): ResponseEntity<Void>{
        val userId = JwtParser.extractKey(request, "userId")
        boardService.deleteHeartInBoard(boardId, userId)

        return ResponseEntity.ok().build()
    }
}