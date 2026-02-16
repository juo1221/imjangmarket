package com.example.imjangmarket.domain.report.dto


/**
 * 임장 보고서 생성을 위한 전체 요청 객체
 */
data class ReportReq(
     val caseNumber: String,          // 사건번호
     val address: String,             // 주소
     val checklist: DynamicChecklist, // 고정 + 자유 추가 체크리스트
     val overallReview: String,       // 총평
     val cautionNotes: String?,       // 주의사항
     val photoUrls: List<String> = emptyList() // 사진 URL들
)

/**
 * 고정 항목과 자유 항목을 모두 포함하는 체크리스트 구조
 */
data class DynamicChecklist(
     val exterior: ExteriorCheck,    // 고정: 건물 외부
     val interior: InteriorCheck,    // 고정: 건물 내부
     val investigation: InvestigationCheck, // 고정: 현장 조사

     /**
      * 사용자가 직접 추가하는 커스텀 필드
      * 예: { "이웃증언": { "grade": "GOOD", "note": "이웃들이 매우 친절함" } }
      */
     val customFields: Map<String, FieldEvaluation> = emptyMap()
)