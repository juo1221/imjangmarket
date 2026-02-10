package com.example.imjangmarket.domain.report.service

import com.example.imjangmarket.domain.report.dto.ReporRes
import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.repository.ReportRepository
import com.example.imjangmarket.global.exception.BaseError
import com.example.imjangmarket.global.result.ServiceResult
import com.example.imjangmarket.global.utils.executeWithResult
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionOperations

@Service
class ReportService(
     private val reportRepository: ReportRepository,
     private val tx: TransactionOperations
) {
     private val caseNumberPattern = Regex("^[0-9]{4}타경[0-9]+$")

     sealed interface ReportError : BaseError {
          object CaseNumberError : ReportError {
               override val code = "001"
               override val msg = "사건번호 형식이맞지 않습니다."
          }

          object AlreadyExists : ReportError {
               override val code = "002"
               override val msg = "이미 기록한 사건번호 입니다. "
          }

          object UnknownError : ReportError {
               override val code = "003"
               override val msg = "알 수 없는 에러입니다."
          }

          object NotExists : ReportError {
               override val code = "002"
               override val msg = "이미 삭제된 보고서 입니다."
          }
     }

     /**
      * 새로운 임장 보고서를 등록합니다.
      * @param request 사용자가 입력한 보고서 데이터
      * @param userId 현재 로그인한 사용자의 ID
      */
     fun createReport(request: ReportRequest, userId: String): ServiceResult<ReporRes> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.CaseNumberError)
          }
          if (reportRepository.existsByCaseNumberAndUserId(request.caseNumber, userId)) {
               return ServiceResult.Failure(ReportError.AlreadyExists)
          }
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.save(request, userId)
                    ServiceResult.Success(ReporRes(id))
               } catch (e: DataIntegrityViolationException) {
                    ServiceResult.Failure(ReportError.AlreadyExists)
               } catch (e: Exception) {
                    ServiceResult.Failure(ReportError.UnknownError)
               }
          }
     }

     fun updateReport(request: ReportRequest, userId: String): ServiceResult<ReporRes> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.CaseNumberError)
          }
          if (!reportRepository.existsByCaseNumberAndUserId(request.caseNumber, userId)) {
               return ServiceResult.Failure(ReportError.NotExists)
          }
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.update(request, userId)
                    ServiceResult.Success(ReporRes(id))
               } catch (e: Exception) {
                    ServiceResult.Failure(ReportError.UnknownError)
               }
          }
     }

     fun deleteReport(reportId: Long): ServiceResult<ReporRes> {
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.delete(reportId)
                    ServiceResult.Success(ReporRes(id))
               } catch (e: Exception) {
                    ServiceResult.Failure(ReportError.UnknownError)
               }
          }
     }

}