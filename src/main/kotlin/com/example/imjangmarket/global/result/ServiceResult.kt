package com.example.imjangmarket.global.result
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.exception.BaseError

sealed class ServiceResult<out T> {
     data class Success<T>(val data: T) : ServiceResult<T>()
     data class Failure(val error: BaseError) : ServiceResult<Nothing>()
}

fun <T> ServiceResult<T>.toApiResponse(): ApiResponse<T> {
     return when (this) {
          is ServiceResult.Success -> ApiResponse.success(this.data)
          is ServiceResult.Failure -> this.error.toApiResponse()
     }
}