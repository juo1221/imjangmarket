package com.example.imjangmarket.domain.report.controller

import com.example.imjangmarket.domain.report.dto.ReporRes
import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.error.ReportError
import com.example.imjangmarket.domain.report.service.ReportService
import com.example.imjangmarket.global.api.ApiResponse
import com.example.imjangmarket.global.config.ApiErrorDescription
import com.example.imjangmarket.global.exception.BaseError
import com.example.imjangmarket.global.result.toApiResponse
import com.example.imjangmarket.global.result.toResponseEntity
import com.example.imjangmarket.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
@Tag(name = "report", description = "보고서 API")

class ReportController(
     private val reportService: ReportService
) {
     @Operation(summary = "보고서 등록 api")
     @PostMapping("/wp")
     @ApiErrorDescription(errorClass = ReportError::class) // <- 딱 한 줄로 끝!
     fun createReport(
          @RequestBody request: ReportRequest,
          @AuthenticationPrincipal userDetails: CustomUserDetails
     ) = reportService.createReport(request, userDetails.id).toResponseEntity()

     @Operation(summary = "보고서 수정 api")
     @PostMapping("/ep")
     fun updateReport(
          @RequestBody request: ReportRequest,
          @AuthenticationPrincipal userDetails: CustomUserDetails
     ) = reportService.updateReport(request, userDetails.id).toResponseEntity()

     @Operation(summary = "보고서 삭제 api")
     @PostMapping("/dp")
     fun deleteReport(reportId:Long) = reportService.deleteReport(reportId).toResponseEntity()
}