package com.example.imjangmarket.global.exception

import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.utils.Loggable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler: Loggable {
     @ExceptionHandler(Exception::class)
     fun handleUnexpectedException(e: Exception): ResponseEntity<ApiResponse<Nothing, CommonError>> {
          log.info{"알 수 없는 서버 에러 발생: $e"}
          return ResponseEntity
               .status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(ApiResponse.error(CommonError.InternalServerError))
     }
}

interface BaseError {
     val code:String
     val msg:String
     fun toApiResponse(): ApiResponse<Nothing, BaseError> = ApiResponse.error(this)
}