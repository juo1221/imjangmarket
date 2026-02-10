package com.example.imjangmarket.domain.report.service

import com.example.imjangmarket.domain.report.dto.ReporRes
import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.repository.ReportRepository
import com.example.imjangmarket.domain.shop.repository.ShopRepository
import com.example.imjangmarket.domain.shop.service.ShopService
import com.example.imjangmarket.global.exception.BaseError
import com.example.imjangmarket.global.result.ServiceResult
import com.example.imjangmarket.global.utils.executeWithResult
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionOperations

@Service
class ReportService(
     private val reportRepository: ReportRepository,
     private val shopRepository: ShopRepository,
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
               override val msg = "동일한 사건번호의 보고서가 이미 있습니다."
          }

          object UnknownError : ReportError {
               override val code = "003"
               override val msg = "알 수 없는 에러입니다."
          }
     }

     /**
      * 새로운 임장 보고서를 등록합니다.
      * @param request 사용자가 입력한 보고서 데이터
      * @param memberRowId 현재 로그인한 사용자의 ID
      */
     fun createReport(request: ReportRequest, memberRowId: Long): ServiceResult<ReporRes> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.CaseNumberError)
          }
          if (reportRepository.existsByCaseNumberAndUserId(request.caseNumber, memberRowId)) {
               return ServiceResult.Failure(ReportError.AlreadyExists)
          }
          val shopRes = shopRepository.findByMemberId(memberRowId)
          if(shopRes === null) return ServiceResult.Failure(ShopService.ShopError.NotFound)
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.save(request, memberRowId, shopRes.id)
                    if (id === null) ServiceResult.Failure(ReportError.AlreadyExists)
                    else ServiceResult.Success(ReporRes(id))
               } catch (e: DataIntegrityViolationException) {
                    println("---DataIntegrityViolationException---")
                    println(e.message)
                    ServiceResult.Failure(ReportError.UnknownError)
               } catch (e: Exception) {
                    println("---Exception---")
                    println(e.message)
                    ServiceResult.Failure(ReportError.UnknownError)
               }
          }
     }

     fun updateReport(request: ReportRequest, memberRowId: Long): ServiceResult<ReporRes> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.CaseNumberError)
          }
          if (!reportRepository.existsByCaseNumberAndUserId(request.caseNumber, memberRowId)) {
               return ServiceResult.Failure(ReportError.AlreadyExists)
          }
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.update(request, memberRowId)
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
                    ServiceResult.Success(ReporRes(id.toLong()))
               } catch (e: Exception) {
                    ServiceResult.Failure(ReportError.UnknownError)
               }
          }
     }

}