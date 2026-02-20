package com.example.imjangmarket.domain.report.repository

import com.example.imjangmarket.domain.report.dto.PurchaseStatusDto
import com.example.imjangmarket.domain.report.dto.ReportReq
import com.example.imjangmarket.domain.report.dto.ReportDetailRes
import com.example.imjangmarket.domain.report.dto.ReportSimpleRes
import com.example.imjangmarket.jooq.tables.references.PURCHASE
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import com.example.imjangmarket.jooq.tables.references.REPORT
import kotlin.jvm.java


@Repository
class ReportRepository(
     private val dsl: DSLContext
) {
     // Kotlin 객체 <-> JSON 변환을 위한 매퍼
     private val objectMapper = ObjectMapper().registerKotlinModule()

     /**
      * 특정 유저가 해당 사건번호로 작성한 보고서가 존재하는지 효율적으로 확인합니다.
      */
     fun existsByCaseNumberAndUserId(caseNumber: String, memberRowId: Long): Boolean {
          return dsl.fetchExists(
               dsl.selectFrom(REPORT)
                    .where(REPORT.CASE_NUMBER.eq(caseNumber))
                    .and(REPORT.MEMBER_ID.eq(memberRowId))
          )
     }

     fun findPurchaseStatus(reportId: Long, memberRowId: Long): PurchaseStatusDto? {
          return dsl.select(
               REPORT.ID,
               PURCHASE.ID.`as`("purchaseId"),
               REPORT.MEMBER_ID.eq(memberRowId).`as`("isOwner")
          )
               .from(REPORT)
               .leftJoin(PURCHASE)
               .on(PURCHASE.REPORT_ID.eq(REPORT.ID))
               .and(PURCHASE.BUYER_ID.eq(memberRowId))
               .where(REPORT.ID.eq(reportId))
               .fetchOneInto(PurchaseStatusDto::class.java)
     }

     /**
      * 보고서 저장
      * JSONB 덕분에 customFields가 아무리 늘어나도 쿼리는 변하지 않습니다.
      */
     fun save(request: ReportReq, memberRowId: Long, shopId: Long): Long? {
          val checklistJson = objectMapper.writeValueAsString(request.checklist)
          return dsl.insertInto(REPORT)
               .set(REPORT.CASE_NUMBER, request.caseNumber)
               .set(REPORT.MEMBER_ID, memberRowId)
               .set(REPORT.ADDRESS, request.address)
               .set(REPORT.CHECKLIST, JSONB.valueOf(checklistJson)) // 전체 체크리스트를 JSONB로 저장
               .set(REPORT.OVERALL_REVIEW, request.overallReview)
               .set(REPORT.CAUTION_NOTES, request.cautionNotes)
               .set(REPORT.PHOTO_URLS, request.photoUrls.toTypedArray())
               .set(REPORT.SHOP_ID, shopId)
               .set(REPORT.CREATED_AT, LocalDateTime.now())
               .set(REPORT.UPDATED_AT, LocalDateTime.now())
               .returning(REPORT.ID) // [핵심] 생성된 ID를 반환하도록 지정
               .fetchOne()      // 한 건의 레코드를 가져옴
               ?.id
     }

     /**
      * 보고서 업데이트
      */
     fun update(request: ReportReq, memberRowId: Long): Long {
          println("request.caseNum: ${request.caseNumber}")
          println("request.memberRowId: $memberRowId")
          val checklistJson = objectMapper.writeValueAsString(request.checklist)
          return dsl.update(REPORT)
               .set(REPORT.CHECKLIST, JSONB.valueOf(checklistJson)) // 전체 체크리스트를 JSONB로 저장
               .set(REPORT.OVERALL_REVIEW, request.overallReview)
               .set(REPORT.CAUTION_NOTES, request.cautionNotes)
               .set(REPORT.PHOTO_URLS, request.photoUrls.toTypedArray())
               .set(REPORT.CREATED_AT, LocalDateTime.now())
               .set(REPORT.UPDATED_AT, LocalDateTime.now())
               .where(REPORT.CASE_NUMBER.eq(request.caseNumber)).and(REPORT.MEMBER_ID.eq(memberRowId))
               .returning(REPORT.ID) // [핵심] 생성된 ID를 반환하도록 지정
               .fetchOne()      // 한 건의 레코드를 가져옴
               ?.id ?: throw RuntimeException("수정 실패")

     }

     /**
      * 보고서 삭제
      */
     fun delete(reportId: Long): Int {
          return dsl
               .deleteFrom(REPORT)
               .where(REPORT.ID.eq(reportId))
               .execute()
     }

     /** 보고서 조회*/
     fun findListByShopId(shopId: Long): List<ReportSimpleRes> {
          return dsl.select(
               REPORT.ID,
               REPORT.CASE_NUMBER,
               REPORT.SHOP_ID,
               REPORT.CREATED_AT,
               REPORT.UPDATED_AT
          )
               .from(REPORT)
               .where(REPORT.SHOP_ID.eq(shopId))
               .fetchInto(ReportSimpleRes::class.java)
     }

     fun findListByCaseNumber(caseNumber: String): List<ReportSimpleRes> {
          return dsl.select(
               REPORT.ID,
               REPORT.CASE_NUMBER,
               REPORT.SHOP_ID,
               REPORT.CREATED_AT,
               REPORT.UPDATED_AT
          )
               .from(REPORT)
               .where(REPORT.CASE_NUMBER.eq(caseNumber))
               .fetchInto(ReportSimpleRes::class.java)
     }

     fun findListAll(): List<ReportSimpleRes> {
          return dsl.select(
               REPORT.ID,
               REPORT.CASE_NUMBER,
               REPORT.SHOP_ID,
               REPORT.CREATED_AT,
               REPORT.UPDATED_AT
          )
               .from(REPORT)
               .fetchInto(ReportSimpleRes::class.java)
     }

     /** 보고서 상세 조회*/
     fun findOneById(id: Long): ReportDetailRes? {
          return dsl.selectFrom(REPORT)
               .where(REPORT.ID.eq(id))
               .fetchOneInto(ReportDetailRes::class.java)
     }

     fun findOneByCaseNumber(caseNumber: String, memberRowId: Long): ReportDetailRes? {
          return dsl.selectFrom(REPORT)
               .where(REPORT.CASE_NUMBER.eq(caseNumber)).and(REPORT.MEMBER_ID.eq(memberRowId))
               .fetchOneInto(ReportDetailRes::class.java)
     }
}