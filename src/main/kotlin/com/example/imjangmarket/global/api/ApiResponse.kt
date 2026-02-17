package com.example.imjangmarket.global.api

import com.example.imjangmarket.global.exception.BaseError
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공통 응답 객체")
data class ApiResponse<out T>(
     val success: Boolean,
     val data: T? = null,
     val error: ErrorDetail? = null
) {
     companion object {
          fun <T> success(data: T?): ApiResponse<T> = ApiResponse(true, data)

          // 어떤 에러 객체가 오더라도 code와 message 문자열로 변환하여 저장
          fun error(code: String, message: String): ApiResponse<Nothing> =
               ApiResponse(
                    success = false,
                    error = ErrorDetail(code, message)
               )
     }
}

@Schema(description = "에러 상세 정보")
data class ErrorDetail(
     @Schema(description = "에러 코드", example = "AUTH_001")
     val code: String,
     @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
     val message: String
)