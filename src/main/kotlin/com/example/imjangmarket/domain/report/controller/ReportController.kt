package com.example.imjangmarket.domain.report.controller

import com.example.imjangmarket.domain.report.dto.ReportCreateRes
import com.example.imjangmarket.domain.report.dto.ReportDeleteRes
import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.dto.ReportUpdateRes
import com.example.imjangmarket.domain.report.service.ReportService
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.result.toApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportController(
     private val reportService: ReportService
) {
     @PostMapping("/cp")
     fun createReport(
          @RequestBody request: ReportRequest,
          @AuthenticationPrincipal userId: String
     ): ApiResponse<ReportCreateRes> = reportService.createReport(request, userId).toApiResponse()

     @PostMapping("/ep")
     fun updateReport(
          @RequestBody request: ReportRequest,
          @AuthenticationPrincipal userId: String
     ): ApiResponse<ReportUpdateRes> = reportService.updateReport(request, userId).toApiResponse()

     @PostMapping("/dp")
     fun deleteReport(
          @AuthenticationPrincipal reportId: String
     ): ApiResponse<ReportDeleteRes> = reportService.deleteReport(reportId).toApiResponse()
}