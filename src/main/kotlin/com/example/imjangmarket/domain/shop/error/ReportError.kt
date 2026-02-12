package com.example.imjangmarket.domain.shop.error

import com.example.imjangmarket.domain.report.error.ReportError
import com.example.imjangmarket.global.exception.BaseError
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
     description = "보고서 도메인 에러 코드",
     subTypes = [
          ReportError.AlreadyExists::class,
          ReportError.NotFound::class,
          ReportError.InvalidCaseNumber::class
     ]
)

sealed interface ShopError : BaseError {
     @Schema(description = "없는 상점")
     object NotExist : ShopError {
          override val code = "SHOP_001"
          override val msg = "존재하지 않는 상점입니다."
     }
}
