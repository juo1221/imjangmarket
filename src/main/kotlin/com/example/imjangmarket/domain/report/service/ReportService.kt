package com.example.imjangmarket.domain.report.service

import com.example.imjangmarket.domain.report.dto.IdFieldDto
import com.example.imjangmarket.domain.report.dto.ReportDetailRes
import com.example.imjangmarket.domain.report.dto.ReportListRes
import com.example.imjangmarket.domain.report.dto.ReportReq
import com.example.imjangmarket.domain.report.error.ReportError
import com.example.imjangmarket.domain.report.repository.ReportRepository
import com.example.imjangmarket.domain.shop.error.ShopError
import com.example.imjangmarket.domain.shop.repository.ShopRepository
import com.example.imjangmarket.global.result.ServiceResult
import com.example.imjangmarket.global.utils.Loggable
import com.example.imjangmarket.global.utils.executeWithResult
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionOperations

@Service
class ReportService(
     private val reportRepository: ReportRepository,
     private val shopRepository: ShopRepository,
     private val tx: TransactionOperations
) : Loggable {
     private val caseNumberPattern = Regex("^[0-9]{4}타경[0-9]+$")

     fun listByShopId(shopId: Long): ServiceResult<ReportListRes> {
          val reportRes = reportRepository.listByShopId(shopId)
          return if (reportRes.isNotEmpty()) ServiceResult.Success(ReportListRes(reportRes)) else ServiceResult.Failure(
               ReportError.EmptyList
          )
     }

     fun getListByCaseNumber(caseNumber: String): ServiceResult<ReportListRes> {
          if (!caseNumberPattern.matches(caseNumber)) {
               return ServiceResult.Failure(ReportError.InvalidCaseNumber)
          }
          val reportRes = reportRepository.listByCaseNumber(caseNumber)
          return if (reportRes.isNotEmpty()) ServiceResult.Success(ReportListRes(reportRes)) else ServiceResult.Failure(
               ReportError.EmptyList
          )
     }

     fun getReportWithAuthority(reportId: Long, memberRowId: Long): ServiceResult<ReportDetailRes> {
          val result = reportRepository.findPurchaseStatus(reportId, memberRowId) ?: return ServiceResult.Failure(error = ReportError.NotFound)
          return if (result.isOwner || result.purchaseId != null) {
               val reportRes = reportRepository.findById(reportId) ?: run {
                    log.error { "데이터 정합성 오류" }
                    return ServiceResult.Failure(error = ReportError.SystemInconsistency)
               }
               ServiceResult.Success(data = reportRes)

          } else ServiceResult.Failure(error = ReportError.NotAuthorized)
     }

     /**
      * 새로운 임장 보고서를 등록합니다.
      * @param request 사용자가 입력한 보고서 데이터
      * @param memberRowId 현재 로그인한 사용자의 ID
      */
     fun createReport(request: ReportReq, memberRowId: Long): ServiceResult<IdFieldDto> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.InvalidCaseNumber)
          }
          if (reportRepository.existsByCaseNumberAndUserId(request.caseNumber, memberRowId)) {
               return ServiceResult.Failure(ReportError.AlreadyExists)
          }
          val shopRes = shopRepository.findByMemberId(memberRowId)
          if (shopRes === null) return ServiceResult.Failure(ShopError.NotExist)
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.save(request, memberRowId, shopRes.id)
                    if (id === null) ServiceResult.Failure(ReportError.AlreadyExists)
                    else ServiceResult.Success(IdFieldDto(id))
               } catch (e: DataIntegrityViolationException) {
                    log.info { "사건번호 중복 또는 DB 제약 조건 위반: ${request.caseNumber} ,$e" }
                    ServiceResult.Failure(ReportError.Unknown)
               } catch (e: Exception) {
                    log.info { "예기치못한 에러 발생: $e" }
                    ServiceResult.Failure(ReportError.Unknown)
               }
          }
     }

     fun updateReport(request: ReportReq, memberRowId: Long): ServiceResult<IdFieldDto> {
          if (!caseNumberPattern.matches(request.caseNumber)) {
               return ServiceResult.Failure(ReportError.InvalidCaseNumber)
          }
          if (!reportRepository.existsByCaseNumberAndUserId(request.caseNumber, memberRowId)) {
               return ServiceResult.Failure(ReportError.AlreadyExists)
          }
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.update(request, memberRowId)
                    ServiceResult.Success(IdFieldDto(id))
               } catch (e: Exception) {
                    ServiceResult.Failure(ReportError.InvalidCaseNumber)
               }
          }
     }

     fun deleteReport(reportId: Long, memberRowId: Long): ServiceResult<IdFieldDto> {
          val report = reportRepository.findById(reportId) ?: return ServiceResult.Failure(ReportError.DeletedReport)
          if(report.memberId != memberRowId) return ServiceResult.Failure(ReportError.NoDeleteAuthority)
          return tx.executeWithResult { status ->
               try {
                    val id = reportRepository.delete(reportId)
                    if(id == 0) throw Exception("데이터 정합성 오류")
                    else ServiceResult.Success(IdFieldDto(id.toLong()))
               } catch (e: Exception) {
                    log.info { "예기치못한 에러 발생: $e" }
                    ServiceResult.Failure(ReportError.Unknown)
               }
          }
     }

}