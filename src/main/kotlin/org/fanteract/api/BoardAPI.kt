package org.fanteract.api

import jakarta.servlet.http.HttpServletRequest
import org.fanteract.annotation.LoginRequired
import org.fanteract.config.JwtParser
import org.fanteract.dto.*
import org.fanteract.service.BoardService
import org.springframework.http.ResponseEntity
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
    @GetMapping("/{boardId}/board")
    fun readBoardDetail(
        @PathVariable boardId: Long,
    ): ResponseEntity<ReadBoardDetailResponse>{
        val response = boardService.readBoardDetail(boardId)

        return ResponseEntity.ok().body(response)
    }

    @LoginRequired
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


}