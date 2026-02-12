package com.example.imjangmarket.global.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시스템 공통 에러")
sealed interface CommonError : BaseError {

     @Schema(description = "서버 내부 오류")
     object InternalServerError : CommonError {
          override val code: String = "COMMON_500"
          override val msg: String = "서버에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
     }
}