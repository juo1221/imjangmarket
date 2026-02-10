package com.example.imjangmarket.domain.report.repository

import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.jooq.tables.references.REPORT
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ReportRepository(
     private val dsl: DSLContext
) {
     private val t = REPORT
     // Kotlin 객체 <-> JSON 변환을 위한 매퍼
     private val objectMapper = ObjectMapper().registerKotlinModule()
     /**
      * 특정 유저가 해당 사건번호로 작성한 보고서가 존재하는지 효율적으로 확인합니다.
      */
     fun existsByCaseNumberAndUserId(caseNumber: String, userId: String): Boolean {
          return dsl.fetchExists(
               dsl.selectFrom(t)
                    .where(t.CASE_NUMBER.eq(caseNumber))
                    .and(t.USER_ID.eq(userId))
          )
     }

     /**
      * 보고서 저장
      * JSONB 덕분에 customFields가 아무리 늘어나도 쿼리는 변하지 않습니다.
      */
     fun save(request: ReportRequest, userId: String): Long {
          val checklistJson = objectMapper.writeValueAsString(request.checklist)
          return dsl.insertInto(t)
                    .set(t.CASE_NUMBER, request.caseNumber)
                    .set(t.USER_ID, userId)
                    .set(t.ADDRESS, request.address)
                    .set(t.CHECKLIST, JSONB.valueOf(checklistJson)) // 전체 체크리스트를 JSONB로 저장
                    .set(t.OVERALL_REVIEW, request.overallReview)
                    .set(t.CAUTION_NOTES, request.cautionNotes)
                    .set(t.PHOTO_URLS, request.photoUrls.toTypedArray())
                    .set(t.CREATED_AT, LocalDateTime.now())
                    .set(t.UPDATED_AT, LocalDateTime.now())
                    .returning(t.ID) // [핵심] 생성된 ID를 반환하도록 지정
                    .fetchOne()      // 한 건의 레코드를 가져옴
                    ?.id ?: throw RuntimeException("저장 실패")

     }
     /**
      * 보고서 업데이트
      */
     fun update(request: ReportRequest, userId: String): Long {
          val checklistJson = objectMapper.writeValueAsString(request.checklist)
          return dsl.update(t)
               .set(t.ADDRESS, request.address)
               .set(t.CHECKLIST, JSONB.valueOf(checklistJson)) // 전체 체크리스트를 JSONB로 저장
               .set(t.OVERALL_REVIEW, request.overallReview)
               .set(t.CAUTION_NOTES, request.cautionNotes)
               .set(t.PHOTO_URLS, request.photoUrls.toTypedArray())
               .set(t.CREATED_AT, LocalDateTime.now())
               .set(t.UPDATED_AT, LocalDateTime.now())
               .returning(t.ID) // [핵심] 생성된 ID를 반환하도록 지정
               .fetchOne()      // 한 건의 레코드를 가져옴
               ?.id ?: throw RuntimeException("수정 실패")

     }
     /**
      * 보고서 삭제
      */
     fun delete(reportId: Long): Int {
          return dsl
               .deleteFrom(t)
               .where(t.CASE_NUMBER.eq("$reportId"))
               .execute()
     }
}