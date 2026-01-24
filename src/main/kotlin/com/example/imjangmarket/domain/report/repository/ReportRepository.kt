package com.example.imjangmarket.domain.report.repository

import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.jooq.tables.references.IMJANG_REPORT
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
     // jOOQ가 생성한 테이블 객체 (실제 테이블명에 맞춰 수정 필요)
     private val t = IMJANG_REPORT

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
     fun save(request: ReportRequest, userId: String) {
          val checklistJson = objectMapper.writeValueAsString(request.checklist)
               dsl.insertInto(t)
                    .set(t.CASE_NUMBER, request.caseNumber)
                    .set(t.USER_ID, userId)
                    .set(t.ADDRESS, request.address)
                    .set(t.CHECKLIST, JSONB.valueOf(checklistJson)) // 전체 체크리스트를 JSONB로 저장
                    .set(t.OVERALL_REVIEW, request.overallReview)
                    .set(t.CAUTION_NOTES, request.cautionNotes)
                    .set(t.PHOTO_URLS, request.photoUrls.toTypedArray())
                    .set(t.CREATED_AT, LocalDateTime.now())
                    .set(t.UPDATED_AT, LocalDateTime.now())
                    .execute()
     }
}