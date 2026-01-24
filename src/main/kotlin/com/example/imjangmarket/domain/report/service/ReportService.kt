package com.example.imjangmarket.domain.report.service

import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.repository.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionOperations

@Service
class ReportService(
     private val reportRepository: ReportRepository,
     private val transaction: TransactionOperations,
     private val reportValidator: ReportValidator
) {
     /**
      * 새로운 임장 보고서를 등록합니다.
      * @param request 사용자가 입력한 보고서 데이터
      * @param userId 현재 로그인한 사용자의 ID
      */
     fun createReport(request: ReportRequest, userId: String) {
          reportValidator.validateAll(request, userId)
          transaction.execute {
               reportRepository.save(request, userId)
          }
     }
}