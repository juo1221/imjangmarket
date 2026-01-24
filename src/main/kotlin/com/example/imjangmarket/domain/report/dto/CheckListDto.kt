package com.example.imjangmarket.domain.report.dto

/**
 * 평가 등급 정의 (불량, 보통, 양호, 불가)
 */
enum class EvaluationGrade {
     BAD,    // 불량
     NORMAL, // 보통
     GOOD,   // 양호
     UNKNOWN // 확인불가/정보없음
}

/**
 * 개별 항목 평가 구조 (등급 + 추가 메모)
 */
data class FieldEvaluation(
     val grade: EvaluationGrade = EvaluationGrade.UNKNOWN,
     val note: String? = null
)

data class ExteriorCheck(
     val slope: FieldEvaluation,          // 경사
     val maintenance: FieldEvaluation,    // 건물관리
     val wallCrack: FieldEvaluation,      // 외벽크랙
     val gasValve: FieldEvaluation,       // 가스밸브
     val airConditioner: FieldEvaluation, // 에어컨
     val parking: FieldEvaluation,        // 주차대수
     val view: FieldEvaluation,           // 뷰(방향)
     val chassis: FieldEvaluation,        // 샤시 여부
     val mailCount: FieldEvaluation       // 우편물
)

data class InteriorCheck(
     val wallCrack: FieldEvaluation,      // 내벽크랙
     val doorStatus: FieldEvaluation      // 현관문 교체 여부 및 청결도
)

data class InvestigationCheck(
     val waterLeak: FieldEvaluation,      // 아랫집 누수 확인 방문
     val unpaidFee: String? = null,       // 미납 관리비 (서술형)
     val occupantContact: String? = null  // 점유자 대화 및 특징 (서술형)
)