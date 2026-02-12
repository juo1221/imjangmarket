package com.example.imjangmarket.global.result
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.exception.BaseError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class ServiceResult<out T> {
     data class Success<T>(val data: T) : ServiceResult<T>()
     data class Failure(val error: BaseError) : ServiceResult<Nothing>()
}

fun <T> ServiceResult<T>.toApiResponse(): ApiResponse<T, BaseError> {
     return when (this) {
          is ServiceResult.Success -> ApiResponse.success(this.data)
          is ServiceResult.Failure -> this.error.toApiResponse()
     }
}

fun <T> ServiceResult<T>.toResponseEntity(): ResponseEntity<ApiResponse<T, BaseError>> {
     return when (this) {
          is ServiceResult.Success -> ResponseEntity.ok(this.toApiResponse())
          is ServiceResult.Failure -> ResponseEntity
               .status(HttpStatus.BAD_REQUEST) // 여기서 400 상태 코드를 지정합니다.
               .body(this.toApiResponse())
     }
}