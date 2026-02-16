package com.example.imjangmarket.domain.report.error
import com.example.imjangmarket.global.exception.BaseError


 interface ReportError : BaseError {
     object AlreadyExists : ReportError {
          override val code: String = "REPORT_001"
          override val msg: String = "이미 등록된 사건번호입니다. 기존 보고서를 확인해 주세요."
     }
     object NotFound : ReportError {
          override val code: String = "REPORT_002"
          override val msg: String = "해당 보고서를 찾을 수 없습니다."
     }
     object InvalidCaseNumber : ReportError {
          override val code: String = "REPORT_003"
          override val msg: String = "사건번호 형식이 올바르지 않습니다. (예: 2024타경12345)"
     }
     object Unknown : ReportError {
          override val code: String = "REPORT_004"
          override val msg: String = "보고서 처리 중 알 수 없는 오류가 발생했습니다."
     }
}