package com.example.imjangmarket.global.exception

import com.example.imjangmarket.global.api.ApiResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
     @ExceptionHandler(BusinessException::class)
     fun handleBusinessException(e: BusinessException) = ErrorResponse(e.message)

     @ExceptionHandler(Exception::class)
     fun handleAllException(e: Exception) = ErrorResponse("서버 내부 오류가 발생했습니다.")
}

data class ErrorResponse(
     val message: String
)

interface BaseError {
     val code:String
     val msg:String
     fun toApiResponse(): ApiResponse<Nothing> = ApiResponse.error(code,msg)
}