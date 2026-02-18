package com.example.imjangmarket.domain.report.dto

import java.time.LocalDateTime

data class ReportDetailRes(
     val id: Long,
     val caseNumber: String,
     val memberId: Long,
     val address: String,
     val checklist: DynamicChecklist,
     val overallReview: String,
     val cautionNotes: String,
     val photoUrls: List<String>,
     val shopId: Long,
     val createdAt: LocalDateTime,
     val updatedAt: LocalDateTime
)
data class ReportSimpleRes(
     val id: Long,
     val caseNumber: String,
     val shopId: Long,
     val createdAt: LocalDateTime,
     val updatedAt: LocalDateTime
)
data class ReportListRes(
     val reports: List<ReportSimpleRes>
)

data class PurchaseStatusDto(
     val id: Long?,        // 보고서 ID
     val purchaseId: Long?, // 구매 이력 ID (구매 안 했으면 null)
     val isOwner: Boolean
)