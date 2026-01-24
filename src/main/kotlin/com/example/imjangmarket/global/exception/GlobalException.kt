package com.example.imjangmarket.global.exception

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
     @ExceptionHandler(BusinessException::class)
     fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
          return ResponseEntity
               .status(e.status)
               .body(ErrorResponse(e.message))
     }
     @ExceptionHandler(Exception::class)
     fun handleAllException(e: Exception): ResponseEntity<ErrorResponse> {
          // 실제 운영 환경에서는 로그를 남기고 보안을 위해 메시지를 가립니다.
          return ResponseEntity
               .status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(ErrorResponse("서버 내부 오류가 발생했습니다."))
     }
}

/**
 * 프론트엔드에 내려줄 공통 에러 응답 규격
 */
data class ErrorResponse(
     val message: String
)