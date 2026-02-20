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
          override val msg: String = "보고서 처리 중 알 수 없는 오류가 발생했습니다. 잠시후 다시 시도해 주세요."
     }
      object EmptyList : ReportError {
           override val code: String = "REPORT_005"
           override val msg: String = "조건에 해당하는 보고서가 없습니다."
      }
      object NotAuthorized : ReportError {
           override val code: String = "REPORT_006"
           override val msg: String = "보고서를 먼저 구매해 주세요."
      }
      object SystemInconsistency : ReportError {
           override val code: String = "ERR-RPT-5001"
           override val msg: String = "서비스 오류가 발생했습니다. (에러 코드: ERR-RPT-5001)"
      }
      object DeletedReport : ReportError {
           override val code: String = "REPORT_007"
           override val msg: String = "이미 삭제된 보고서입니다."
      }
      object NoAuthority : ReportError {
           override val code: String = "REPORT_008"
           override val msg: String = "권한이 없습니다."
      }
      object NotChangeableCaseNumber : ReportError {
           override val code: String = "REPORT_009"
           override val msg: String = "사건번호는 변경할 수 없습니다."
      }
      object NotChangeableAddress : ReportError {
           override val code: String = "REPORT_0010"
           override val msg: String = "주소는 변경할 수 없습니다."
      }
}