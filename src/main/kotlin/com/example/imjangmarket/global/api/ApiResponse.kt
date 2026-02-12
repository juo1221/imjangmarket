package com.example.imjangmarket.global.api

import com.example.imjangmarket.global.exception.BaseError

data class ApiResponse<out T, out E : BaseError> (
     val success:Boolean,
     val data: T? = null,
     val error: ErrorResponse<E>? = null
) {
     companion object {
          fun <T> success(data:T?): ApiResponse<T, Nothing> = ApiResponse(true,data)
          fun <E : BaseError> error(err:E, detail:Any? =null): ApiResponse<Nothing, E> =
               ApiResponse(
                    success = false,
                    error = ErrorResponse(err, err.msg, detail)
               )
     }
}

data class ErrorResponse<out E : BaseError>(
     val code: E,
     val message: String,
     val details: Any? = null
)