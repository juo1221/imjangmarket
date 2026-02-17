package com.example.imjangmarket.global.exception

import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.utils.Loggable
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler: Loggable {
     @ExceptionHandler(Exception::class)
     fun handleUnexpectedException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
          log.info{"알 수 없는 서버 에러 발생: $e"}
          val error = CommonError.InternalServerError
          return ResponseEntity
               .status(HttpStatus.INTERNAL_SERVER_ERROR)
               // 수정: 정의된 에러 코드와 메시지를 명시적으로 전달합니다.
               .body(ApiResponse.error(error.code, error.msg))
     }
}
@Schema(hidden = true)
interface BaseError {
     val code:String
     val msg:String
     fun toApiResponse(): ApiResponse<Nothing> = ApiResponse.error(code, msg)}