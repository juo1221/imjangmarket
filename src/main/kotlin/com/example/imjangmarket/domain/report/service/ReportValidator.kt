package com.example.imjangmarket.domain.report.service

import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.repository.ReportRepository
import org.springframework.stereotype.Component

/**
 * 임장 보고서 관련 모든 검증을 전담하는 컴포넌트입니다.
 */
@Component
class ReportValidator(
     private val reportRepository: ReportRepository
) {
     // 사건번호 정규표현식 패턴
     private val caseNumberPattern = Regex("^[0-9]{4}타경[0-9]+$")

     /**
      * 모든 검증을 수행=. (트랜잭션 외부에서 호출)
      */
     fun validateAll(request: ReportRequest, userId: String) {
          validateFormat(request.caseNumber)
          validateDuplicate(request.caseNumber, userId)
     }

     /**
      * 1. 형식 검증 (Static Validation)
      * DB 접속 없이 메모리 내에서 빠르게 처리.
      */
     private fun validateFormat(caseNumber: String) {
          if (!caseNumberPattern.matches(caseNumber)) {
               throw IllegalArgumentException("올바르지 않은 사건번호 형식입니다. (예: 2024타경12345)")
          }
     }

     /**
      * 2. 비즈니스 규칙 검증 (Dynamic Validation)
      * DB 조회가 필요하지만, 읽기 전용 작업이므로 트랜잭션 없이 수행 가능.
      */
     private fun validateDuplicate(caseNumber: String, userId: String) {
          if (reportRepository.existsByCaseNumberAndUserId(caseNumber, userId)) {
               throw IllegalStateException("이미 해당 사건번호($caseNumber)에 대해 작성된 보고서가 존재합니다.")
          }
     }
}