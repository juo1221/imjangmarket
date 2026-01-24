package com.example.imjangmarket.global.api

data class ApiResponse<out T> (
     val success:Boolean,
     val data: T? = null,
     val error: ErrorResponse? = null
) {
     companion object {
          fun <T> success(data:T?): ApiResponse<T> = ApiResponse(true,data)
          fun error(code:String, msg:String, detail:Any? =null): ApiResponse<Nothing> =
               ApiResponse(
                    success = false,
                    error = ErrorResponse(code, msg, detail)
               )
     }
}

data class ErrorResponse(
     val code: String,
     val message: String,
     val details: Any?
)