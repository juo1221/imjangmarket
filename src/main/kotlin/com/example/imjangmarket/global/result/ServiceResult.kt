package com.example.imjangmarket.global.result
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.exception.BaseError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class ServiceResult<out T> {
     data class Success<T>(val data: T) : ServiceResult<T>()
     data class Failure(val error: BaseError) : ServiceResult<Nothing>()
}
fun <T> ServiceResult<T>.toApiResponse(): ApiResponse<T> {
     return when (this) {
          is ServiceResult.Success -> ApiResponse.success(this.data)
          is ServiceResult.Failure -> ApiResponse.error(this.error.code, this.error.msg)
     }
}
fun <T> ServiceResult<T>.toResponseEntity(): ResponseEntity<ApiResponse<T>> {
     return when (this) {
          is ServiceResult.Success -> ResponseEntity.ok(this.toApiResponse())
          is ServiceResult.Failure -> ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(this.toApiResponse())
     }
}

