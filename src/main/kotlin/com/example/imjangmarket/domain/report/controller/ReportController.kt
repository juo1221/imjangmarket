package com.example.imjangmarket.domain.report.controller

import com.example.imjangmarket.domain.report.dto.ReportDeleteReq
import com.example.imjangmarket.domain.report.dto.ReportReq
import com.example.imjangmarket.domain.report.dto.ReportSearchReq
import com.example.imjangmarket.domain.report.service.ReportService
import com.example.imjangmarket.global.result.toResponseEntity
import com.example.imjangmarket.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/report")
@Tag(name = "report", description = "보고서 API")

class ReportController(
     private val reportService: ReportService
) {
     @Operation(summary = "보고서 리스트 조회 api")
     @PostMapping("/list")
     fun listByCaseNumber(@RequestBody req: ReportSearchReq) =
          reportService.getListByCaseNumber(req.caseNumber).toResponseEntity()

     @Operation(summary = "모든 보고서 리스트 조회 api")
     @PostMapping("/list/all")
     fun listByCaseNumber() = reportService.getListAll().toResponseEntity()

     @Operation(summary = "보고서 상세 조회 api")
     @GetMapping("/{reportId}")
     fun getReportDetail(
          @PathVariable reportId : Long,
          @AuthenticationPrincipal userDetails: CustomUserDetails
          ) = reportService.getReportWithAuthority(reportId, userDetails.id).toResponseEntity()

     @Operation(summary = "보고서 등록 api")
     @PostMapping("/wp")
     fun createReport(
          @RequestBody request: ReportReq,
          @AuthenticationPrincipal userDetails: CustomUserDetails
     ) = reportService.createReport(request, userDetails.id).toResponseEntity()

     @Operation(summary = "보고서 수정 api")
     @PostMapping("/ep")
     fun updateReport(
          @RequestBody request: ReportReq,
          @AuthenticationPrincipal userDetails: CustomUserDetails
     ) = reportService.updateReport(request, userDetails.id).toResponseEntity()

     @Operation(summary = "보고서 삭제 api")
     @PostMapping("/dp")
     fun deleteReport(
          @RequestBody request: ReportDeleteReq,
          @AuthenticationPrincipal userDetails: CustomUserDetails
     ) = reportService.deleteReport(request.reportId, userDetails.id).toResponseEntity()
}