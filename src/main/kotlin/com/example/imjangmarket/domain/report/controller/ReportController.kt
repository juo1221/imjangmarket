package com.example.imjangmarket.domain.report.controller

import com.example.imjangmarket.domain.report.dto.ReportRequest
import com.example.imjangmarket.domain.report.service.ReportService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportController(
     private val reportService: ReportService
) {
     @PostMapping()
     fun createReport(
          @RequestBody request: ReportRequest,
          @AuthenticationPrincipal userId: String
     ): ResponseEntity<String>  {
          reportService.createReport(request, userId)
          return ResponseEntity
               .status(HttpStatus.CREATED)
               .body("성공적으로 보고서가 등록되었습니다.")
     }
}