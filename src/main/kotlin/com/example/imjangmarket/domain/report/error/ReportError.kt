package com.example.imjangmarket.domain.report.error

import com.example.imjangmarket.global.exception.BaseError
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
     name = "Error_Report",
     description = "보고서 도메인 에러 코드",
     oneOf = [
          ReportError.AlreadyExists::class,
          ReportError.NotFound::class,
          ReportError.InvalidCaseNumber::class,
          ReportError.Unknown::class
     ]
)
interface ReportError : BaseError {
     @Schema(name = "Error_Report_AlreadyExists", description = "이미 등록된 사건번호 에러")
     object AlreadyExists : ReportError {
          override val code: String = "REPORT_001"
          override val msg: String = "이미 등록된 사건번호입니다. 기존 보고서를 확인해 주세요."
     }
     @Schema(name = "Error_Report_NotFound", description = "보고서를 찾을 수 없음")
     object NotFound : ReportError {
          override val code: String = "REPORT_002"
          override val msg: String = "해당 보고서를 찾을 수 없습니다."
     }
     @Schema(name = "Error_Report_InvalidCaseNumber",description = "잘못된 사건번호 형식")
     object InvalidCaseNumber : ReportError {
          override val code: String = "REPORT_003"
          override val msg: String = "사건번호 형식이 올바르지 않습니다. (예: 2024타경12345)"
     }
     @Schema(name = "Error_Report_Unknown", description = "알 수 없는 에러")
     object Unknown : ReportError {
          override val code: String = "REPORT_004"
          override val msg: String = "보고서 처리 중 알 수 없는 오류가 발생했습니다."
     }
}